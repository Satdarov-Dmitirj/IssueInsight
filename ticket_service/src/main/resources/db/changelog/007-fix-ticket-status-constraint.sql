ALTER TABLE tickets DROP CONSTRAINT IF EXISTS tickets_ticket_status_check;
ALTER TABLE tickets ADD CONSTRAINT tickets_ticket_status_check
CHECK (ticket_status IN ('OPEN', 'IN_PROGRESS', 'RESOLVED', 'CLOSED'));