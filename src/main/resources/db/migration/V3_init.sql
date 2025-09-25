-- =========================================================
-- GYM: actividades, roles por actividad, notas y facturación
-- ÚNICO ARCHIVO • PostgreSQL • Idempotente
-- Tablas previas esperadas: users, time_slots, reservations
-- =========================================================

-- 0) Utilidades UUID (solo necesario para gen_random_uuid)
CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- 1) Catálogo de actividades
CREATE TABLE IF NOT EXISTS activity (
                                        id     UUID PRIMARY KEY,
                                        code   VARCHAR(64)  NOT NULL UNIQUE,     -- p.ej. 'BJJ', 'BOXING'
    name   VARCHAR(128) NOT NULL,            -- p.ej. 'Brazilian Jiu-Jitsu'
    active BOOLEAN      NOT NULL DEFAULT TRUE
    );

-- 2) Rol por actividad (no confundir con Role global: ADMIN/COACH/MEMBER)
CREATE TABLE IF NOT EXISTS user_activity_role (
                                                  id          UUID PRIMARY KEY,
                                                  user_id     UUID NOT NULL,
                                                  activity_id UUID NOT NULL,
                                                  role_type   VARCHAR(16) NOT NULL CHECK (role_type IN ('TEACHES','LEARNS')),
    CONSTRAINT fk_uar_user     FOREIGN KEY (user_id)     REFERENCES users(id),
    CONSTRAINT fk_uar_activity FOREIGN KEY (activity_id)  REFERENCES activity(id),
    CONSTRAINT uq_uar UNIQUE (user_id, activity_id, role_type)
    );

CREATE INDEX IF NOT EXISTS idx_uar_user          ON user_activity_role(user_id);
CREATE INDEX IF NOT EXISTS idx_uar_activity_role ON user_activity_role(activity_id, role_type);

-- 3) Adaptar time_slots: añadir activity_id y coach_id (NULL primero)
ALTER TABLE time_slots ADD COLUMN IF NOT EXISTS activity_id UUID;
ALTER TABLE time_slots ADD COLUMN IF NOT EXISTS coach_id    UUID;

DO $$
BEGIN
  IF NOT EXISTS (
    SELECT 1 FROM pg_constraint
    WHERE conname = 'fk_timeslot_activity'
  ) THEN
ALTER TABLE time_slots
    ADD CONSTRAINT fk_timeslot_activity FOREIGN KEY (activity_id) REFERENCES activity(id);
END IF;

  IF NOT EXISTS (
    SELECT 1 FROM pg_constraint
    WHERE conname = 'fk_timeslot_coach'
  ) THEN
ALTER TABLE time_slots
    ADD CONSTRAINT fk_timeslot_coach FOREIGN KEY (coach_id) REFERENCES users(id);
END IF;
END $$;

CREATE INDEX IF NOT EXISTS idx_timeslot_activity ON time_slots(activity_id);
CREATE INDEX IF NOT EXISTS idx_timeslot_coach    ON time_slots(coach_id);

-- 4) Semilla mínima y backfill de activity_id
INSERT INTO activity (id, code, name, active)
VALUES (gen_random_uuid(), 'GENERAL', 'General', TRUE)
    ON CONFLICT (code) DO NOTHING;

WITH a AS (SELECT id FROM activity WHERE code = 'GENERAL' LIMIT 1)
UPDATE time_slots
SET activity_id = (SELECT id FROM a)
WHERE activity_id IS NULL;

-- 4.1 Backfill de coach_id (opcional automático):
-- intentará asignar el primer usuario con role='COACH' a los slots sin coach.
-- Si tu tabla users no tiene columna "role" o no hay coaches, no hace nada.
DO $$
DECLARE
coach UUID;
BEGIN
BEGIN
SELECT u.id INTO coach FROM users u
WHERE CAST(u.role AS TEXT) = 'COACH'  -- ajusta si tu columna role es texto ya
    LIMIT 1;
EXCEPTION WHEN undefined_column THEN
    -- si no existe columna role, no hacemos nada
    coach := NULL;
END;

  IF coach IS NOT NULL THEN
UPDATE time_slots SET coach_id = coach WHERE coach_id IS NULL;
END IF;
END $$;

-- 4.2 Forzar NOT NULL SOLO si ya no quedan NULLs
DO $$
BEGIN
  IF NOT EXISTS (SELECT 1 FROM time_slots WHERE activity_id IS NULL) THEN
    EXECUTE 'ALTER TABLE time_slots ALTER COLUMN activity_id SET NOT NULL';
END IF;
  IF NOT EXISTS (SELECT 1 FROM time_slots WHERE coach_id IS NULL) THEN
    EXECUTE 'ALTER TABLE time_slots ALTER COLUMN coach_id SET NOT NULL';
END IF;
END $$;

-- 5) Notas de progreso (profesor -> alumno)
CREATE TABLE IF NOT EXISTS progress_note (
                                             id                 UUID PRIMARY KEY,
                                             activity_id        UUID NOT NULL,
                                             teacher_id         UUID NOT NULL,
                                             student_id         UUID NOT NULL,
                                             created_at         TIMESTAMPTZ NOT NULL,
                                             title              VARCHAR(128),
    comment            TEXT NOT NULL,
    metrics_json       TEXT,
    visible_to_student BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_pn_activity FOREIGN KEY (activity_id) REFERENCES activity(id),
    CONSTRAINT fk_pn_teacher  FOREIGN KEY (teacher_id)  REFERENCES users(id),
    CONSTRAINT fk_pn_student  FOREIGN KEY (student_id)  REFERENCES users(id),
    CONSTRAINT chk_pn_diff CHECK (teacher_id <> student_id)
    );

CREATE INDEX IF NOT EXISTS idx_pn_student ON progress_note(student_id, activity_id, created_at);
CREATE INDEX IF NOT EXISTS idx_pn_teacher ON progress_note(teacher_id, activity_id, created_at);

-- 6) Precios por actividad (vigencias)
CREATE TABLE IF NOT EXISTS activity_price (
                                              id          UUID PRIMARY KEY,
                                              activity_id UUID NOT NULL,
                                              amount      NUMERIC(10,2) NOT NULL CHECK (amount >= 0),
    active_from DATE NOT NULL,
    active_to   DATE,
    CONSTRAINT fk_ap_activity FOREIGN KEY (activity_id) REFERENCES activity(id),
    CONSTRAINT chk_ap_range CHECK (active_to IS NULL OR active_to >= active_from)
    );
CREATE INDEX IF NOT EXISTS idx_ap_activity ON activity_price(activity_id, active_from, active_to);

-- 7) Plan mensual del alumno
CREATE TABLE IF NOT EXISTS student_monthly_plan (
                                                    id            UUID PRIMARY KEY,
                                                    student_id    UUID NOT NULL,
                                                    year_month    CHAR(7) NOT NULL,    -- 'YYYY-MM'
    status        VARCHAR(16) NOT NULL CHECK (status IN ('PAID','UNPAID','PARTIAL')) DEFAULT 'UNPAID',
    paid_at       TIMESTAMPTZ,
    total_amount  NUMERIC(10,2) NOT NULL DEFAULT 0,
    notes         TEXT,
    CONSTRAINT fk_smp_student       FOREIGN KEY (student_id) REFERENCES users(id),
    CONSTRAINT uq_smp_student_month UNIQUE (student_id, year_month)
    );

CREATE TABLE IF NOT EXISTS student_monthly_plan_activity (
                                                             id          UUID PRIMARY KEY,
                                                             plan_id     UUID NOT NULL,
                                                             activity_id UUID NOT NULL,
                                                             CONSTRAINT fk_smpa_plan     FOREIGN KEY (plan_id)    REFERENCES student_monthly_plan(id) ON DELETE CASCADE,
    CONSTRAINT fk_smpa_activity FOREIGN KEY (activity_id) REFERENCES activity(id),
    CONSTRAINT uq_smpa          UNIQUE (plan_id, activity_id)
    );

CREATE TABLE IF NOT EXISTS student_monthly_plan_line (
                                                         id      UUID PRIMARY KEY,
                                                         plan_id UUID NOT NULL,
                                                         label   VARCHAR(128) NOT NULL,
    amount  NUMERIC(10,2) NOT NULL,
    CONSTRAINT fk_smpline_plan FOREIGN KEY (plan_id) REFERENCES student_monthly_plan(id) ON DELETE CASCADE
    );
CREATE INDEX IF NOT EXISTS idx_smpline_plan ON student_monthly_plan_line(plan_id);
