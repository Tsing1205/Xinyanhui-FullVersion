CREATE VIEW AppointmentsWithNames AS
SELECT 
    a.*,
    c.name AS consultant_name,
    u.username AS user_username
FROM 
    Appointments a
JOIN 
    Consultants c ON a.consultant_id = c.consultant_id
JOIN 
    Users u ON a.user_id = u.user_id;

CREATE VIEW ConsultationSessionsWithNames AS
SELECT 
    cs.*,
    c.name AS consultant_name,
    u.username AS user_username
FROM 
    ConsultationSessions cs
JOIN 
    Consultants c ON cs.consultant_id = c.consultant_id
JOIN 
    Users u ON cs.user_id = u.user_id;

CREATE VIEW SupervisorConsultationsWithNames AS
SELECT 
    sc.*,
    c.name AS consultant_name,
    s.name AS supervisor_name
FROM 
    SupervisorConsultations sc
JOIN 
    Consultants c ON sc.consultant_id = c.consultant_id
JOIN 
    Supervisors s ON sc.supervisor_id = s.supervisor_id;