ALTER TABLE patient
    ADD INDEX (first_name, last_name, id);

ALTER TABLE visit
    ADD INDEX (patient_id, doctor_id, start_date_time);
