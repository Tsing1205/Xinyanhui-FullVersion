CREATE TABLE `Users` (
  `user_id` INT AUTO_INCREMENT PRIMARY KEY,
  `username` VARCHAR(20) UNIQUE NOT NULL,
  `password_HashwithSalt` VARCHAR(255) NOT NULL,
  `salt` VARCHAR(255) NOT NULL,
  `email` VARCHAR(100) DEFAULT NULL,
  `phone` VARCHAR(20) DEFAULT NULL,
  `register_date` DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE `Consultants` (
  `consultant_id` INT AUTO_INCREMENT PRIMARY KEY,
  `name` VARCHAR(50) NOT NULL,
  `professional_info` TEXT DEFAULT NULL,
  `password_HashwithSalt` VARCHAR(255) NOT NULL,
  `salt` VARCHAR(255) NOT NULL
);

CREATE TABLE `Admins` (
  `admin_id` INT AUTO_INCREMENT PRIMARY KEY,
  `username` VARCHAR(20) UNIQUE NOT NULL,
  `password_HashwithSalt` VARCHAR(255) NOT NULL,
  `salt` VARCHAR(255) NOT NULL
);

CREATE TABLE `Supervisors` (
  `supervisor_id` INT AUTO_INCREMENT PRIMARY KEY,
  `name` VARCHAR(50) NOT NULL,
  `professional_info` TEXT DEFAULT NULL,
  `password_HashwithSalt` VARCHAR(255) NOT NULL,
  `salt` VARCHAR(255) NOT NULL
);

CREATE TABLE `SupervisorSchedules` (
  `schedule_id` INT AUTO_INCREMENT PRIMARY KEY,
  `supervisor_id` INT NOT NULL,
  `available_date` DATE NOT NULL,
  `start_time` INT NOT NULL,
  `end_time` INT NOT NULL,
  `slot_capacity` INT DEFAULT 5,
  CHECK (start_time >= 0 AND start_time <= 24),
  CHECK (end_time >= 0 AND end_time <= 24),
  FOREIGN KEY (`supervisor_id`) REFERENCES `Supervisors` (`supervisor_id`)
);

CREATE TABLE `Appointments` (
  `appointment_id` INT AUTO_INCREMENT PRIMARY KEY,
  `user_id` INT NOT NULL,
  `consultant_id` INT NOT NULL,
  `appointment_date` DATE NOT NULL,
  `appointment_time` TIME NOT NULL,
  `booking_date` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `status` ENUM('booked','canceled','completed') NOT NULL,
  `cancellation_time` DATETIME DEFAULT NULL,
  `cancellation_reason` TEXT DEFAULT NULL,
  FOREIGN KEY (`user_id`) REFERENCES `Users` (`user_id`),
  FOREIGN KEY (`consultant_id`) REFERENCES `Consultants` (`consultant_id`)
);

-- 预约咨询：预约时间前一小时创建会话，状态置为'ready'，并将end_time置为预约时间加一小时
-- --start_time=CONCAT(appointment_date, ' ', appointment_time);
-- --end_time = DATE_ADD(start_time, INTERVAL 1 HOUR);
-- 实时咨询：咨询开始时start_time置为当前时间，end_time置为NULL，咨询结束后创建对应预约记录
-- --appointment_date = DATE(start_time); appointment_time = TIME(start_time)
-- --booking_date = start_time; status = 'completed'
CREATE TABLE `ConsultationSessions` (
  `session_id` INT AUTO_INCREMENT PRIMARY KEY,
  `appointment_id` INT DEFAULT NULL,
  `user_id` INT NOT NULL,
  `consultant_id` INT NOT NULL,
  `start_time` DATETIME NOT NULL,
  `end_time` DATETIME DEFAULT NULL,
  `chat_log` TEXT NOT NULL,
  `session_status` ENUM('active', 'completed', 'ready') NOT NULL,
  `rating` TINYINT DEFAULT NULL,
  `feedback` TEXT DEFAULT NULL,
  FOREIGN KEY (`user_id`) REFERENCES `Users` (`user_id`),
  FOREIGN KEY (`consultant_id`) REFERENCES `Consultants` (`consultant_id`),
  FOREIGN KEY (`appointment_id`) REFERENCES `Appointments` (`appointment_id`)
);

CREATE TABLE `ConsultantSchedules` (
  `schedule_id` INT AUTO_INCREMENT PRIMARY KEY,
  `consultant_id` INT NOT NULL,
  `available_date` DATE NOT NULL,
  `start_time` INT NOT NULL,
  `end_time` INT NOT NULL,
  `slot_capacity` INT DEFAULT 5,
  `status` ENUM('normal', 'request', 'leave'),
  CHECK (start_time >= 0 AND start_time <= 24),
  CHECK (end_time >= 0 AND end_time <= 24),
  FOREIGN KEY (`consultant_id`) REFERENCES `Consultants` (`consultant_id`)
);

CREATE TABLE `PsychologicalEvaluations` (
  `evaluation_id` INT AUTO_INCREMENT PRIMARY KEY,
  `user_id` INT NOT NULL,
  `evaluation_date` DATETIME NOT NULL,
  `questionnaire_results` JSON NOT NULL,
  `auto_report` TEXT DEFAULT NULL,
  `risk_level` ENUM('low', 'medium', 'high') NOT NULL,
  `suggestions` TEXT DEFAULT NULL,
  FOREIGN KEY (`user_id`) REFERENCES `Users` (`user_id`)
);

CREATE TABLE `SupervisorConsultations` (
  `record_id` INT AUTO_INCREMENT PRIMARY KEY,
  `consultant_id` INT NOT NULL,
  `supervisor_id` INT NOT NULL,
  `session_id` INT NOT NULL,
  `request_time` DATETIME NOT NULL,
  `response_time` DATETIME DEFAULT NULL,
  `chat_log` TEXT NOT NULL,
  FOREIGN KEY (`consultant_id`) REFERENCES `Consultants` (`consultant_id`),
  FOREIGN KEY (`supervisor_id`) REFERENCES `Supervisors` (`supervisor_id`),
  FOREIGN KEY (`session_id`) REFERENCES `ConsultationSessions` (`session_id`)
);

-- 表示咨询师当前是否能够接入实时咨询的视图（包括预约咨询将满一小时，是否能继续咨询）
-- 前置条件：预约咨询开始前一小时，创建会话（`ConsultationSessions`的记录）
-- 每次查询视图时会基于当前时间和最新基础表重新计算，无需额外设置更新
CREATE VIEW CurrentAvailability AS
SELECT
	`cs`.`consultant_id` AS `consultant_id`,
	COALESCE ( sum(( CASE WHEN ( `css`.`session_status` = 'active' ) THEN 1 ELSE 0 END )), 0 ) AS `active_sessions`,
	COALESCE ( sum(( CASE WHEN ( `css`.`session_status` = 'ready' ) THEN 1 ELSE 0 END )), 0 ) AS `ready_sessions`,(
	CASE
			
			WHEN ((
					COALESCE ( sum(( CASE WHEN ( `css`.`session_status` = 'active' ) THEN 1 ELSE 0 END )), 0 ) < `cs`.`slot_capacity` 
					) 
				AND ((
						timestampdiff(
							MINUTE,
							now(),((
									now() + INTERVAL 1 HOUR 
									) - INTERVAL MINUTE (
									now()) MINUTE 
							)) >= 30 
						) 
				OR ( COALESCE ( sum(( CASE WHEN ( `css`.`session_status` = 'ready' ) THEN 1 ELSE 0 END )), 0 ) < `cs`.`slot_capacity` ))) THEN
				1 ELSE 0 
			END 
			) AS `is_available` 
		FROM
			(
				`consultantschedules` `cs`
				LEFT JOIN `consultationsessions` `css` ON (((
							`cs`.`consultant_id` = `css`.`consultant_id` 
							) 
						AND ( cast( `css`.`start_time` AS DATE ) = `cs`.`available_date` ) 
						AND (
							HOUR ( `css`.`start_time` ) BETWEEN `cs`.`start_time` 
						AND ( `cs`.`end_time` - 1 ))))) 
		WHERE
			((
					`cs`.`available_date` = curdate()) 
				AND (
					`cs`.`start_time` <= HOUR (
					now())) 
				AND ( HOUR ( now()) < `cs`.`end_time` ) 
			AND ( `cs`.`status` = 'normal' )) 
		GROUP BY
			`cs`.`consultant_id`,
			`cs`.`slot_capacity`;

-- 查询接下来六天所有咨询师在每个时段是否有空的视图
CREATE VIEW ConsultantDailyAvailability AS
WITH RECURSIVE DateRange AS (
    SELECT CURDATE() + INTERVAL 1 DAY AS available_date
    UNION ALL
    SELECT available_date + INTERVAL 1 DAY
    FROM DateRange
    WHERE available_date < CURDATE() + INTERVAL 6 DAY
),
Hours AS (
    SELECT 8 AS hour UNION ALL SELECT 9 UNION ALL SELECT 10 UNION ALL SELECT 11 UNION ALL SELECT 12
    UNION ALL SELECT 13 UNION ALL SELECT 14 UNION ALL SELECT 15 UNION ALL SELECT 16 UNION ALL SELECT 17
),
ConsultantHours AS (
    SELECT 
        c.consultant_id,
        dr.available_date,
        h.hour,
        cs.status,
        cs.start_time,
        cs.end_time,
        cs.slot_capacity
    FROM 
        Consultants c
    CROSS JOIN 
        DateRange dr
    CROSS JOIN 
        Hours h
    INNER JOIN 
        ConsultantSchedules cs 
        ON c.consultant_id = cs.consultant_id 
        AND cs.available_date = dr.available_date 
        AND cs.start_time <= h.hour 
        AND h.hour < cs.end_time 
        AND cs.status = 'normal'
),
AppointmentsCount AS (
    SELECT 
        consultant_id,
        appointment_date,
        HOUR(appointment_time) AS hour,
        COUNT(*) AS appointment_count
    FROM 
        Appointments
    WHERE 
        appointment_date BETWEEN CURDATE() + INTERVAL 1 DAY AND CURDATE() + INTERVAL 6 DAY
        AND HOUR(appointment_time) BETWEEN 8 AND 17
        AND status = 'booked'
    GROUP BY 
        consultant_id, appointment_date, HOUR(appointment_time)
),
HourlyAvailability AS (
    SELECT 
        ch.consultant_id,
        ch.available_date,
        ch.hour,
        CASE 
            WHEN ch.status = 'normal' 
                 AND ch.start_time <= ch.hour 
                 AND ch.hour < ch.end_time 
                 AND (ac.appointment_count IS NULL OR ac.appointment_count < ch.slot_capacity) 
            THEN 1 
            ELSE 0 
        END AS available
    FROM 
        ConsultantHours ch
    LEFT JOIN 
        AppointmentsCount ac 
        ON ch.consultant_id = ac.consultant_id 
        AND ch.available_date = ac.appointment_date 
        AND ch.hour = ac.hour
)
SELECT 
    consultant_id,
    available_date,
    MAX(CASE WHEN hour = 8 THEN available ELSE 0 END) AS hour_8_available,
    MAX(CASE WHEN hour = 9 THEN available ELSE 0 END) AS hour_9_available,
    MAX(CASE WHEN hour = 10 THEN available ELSE 0 END) AS hour_10_available,
    MAX(CASE WHEN hour = 11 THEN available ELSE 0 END) AS hour_11_available,
    MAX(CASE WHEN hour = 12 THEN available ELSE 0 END) AS hour_12_available,
    MAX(CASE WHEN hour = 13 THEN available ELSE 0 END) AS hour_13_available,
    MAX(CASE WHEN hour = 14 THEN available ELSE 0 END) AS hour_14_available,
    MAX(CASE WHEN hour = 15 THEN available ELSE 0 END) AS hour_15_available,
    MAX(CASE WHEN hour = 16 THEN available ELSE 0 END) AS hour_16_available,
    MAX(CASE WHEN hour = 17 THEN available ELSE 0 END) AS hour_17_available
FROM 
    HourlyAvailability
GROUP BY 
    consultant_id, available_date
ORDER BY 
    consultant_id, available_date;