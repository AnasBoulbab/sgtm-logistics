ALTER TABLE IF EXISTS vehicle
    ADD COLUMN IF NOT EXISTS external_id bigint;

CREATE UNIQUE INDEX IF NOT EXISTS uk_vehicle_external_id ON vehicle (external_id);

DO $$
DECLARE
    constraint_record record;
BEGIN
    FOR constraint_record IN
        SELECT constraint_name
        FROM information_schema.table_constraints
        WHERE table_name = 'day_report'
          AND constraint_type = 'UNIQUE'
    LOOP
        EXECUTE 'ALTER TABLE day_report DROP CONSTRAINT ' || quote_ident(constraint_record.constraint_name);
    END LOOP;
END $$;

ALTER TABLE IF EXISTS day_report
    ADD CONSTRAINT uk_day_report_vehicle_date UNIQUE (vehicle_id, date);
