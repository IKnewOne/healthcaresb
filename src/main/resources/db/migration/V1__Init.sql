CREATE TABLE patients (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          first_name VARCHAR(100) NOT NULL,
                          last_name VARCHAR(100) NOT NULL
);

CREATE TABLE doctors (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         first_name VARCHAR(100) NOT NULL,
                         last_name VARCHAR(100) NOT NULL,
                         timezone VARCHAR(50) NOT NULL,
                         total_patients BIGINT NOT NULL DEFAULT 0
);

CREATE TABLE visits (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        patient_id BIGINT NOT NULL,
                        doctor_id BIGINT NOT NULL,
                        start_date_time DATETIME NOT NULL,
                        end_date_time DATETIME NOT NULL,
                        CONSTRAINT fk_patient FOREIGN KEY (patient_id) REFERENCES patients(id),
                        CONSTRAINT fk_doctor FOREIGN KEY (doctor_id) REFERENCES doctors(id)
);

CREATE TABLE patient_doctor_summary (
                                        patient_id BIGINT NOT NULL,
                                        doctor_id BIGINT NOT NULL,
                                        last_visit_id BIGINT NOT NULL,
                                        PRIMARY KEY (patient_id, doctor_id),
                                        CONSTRAINT fk_summary_patient FOREIGN KEY (patient_id) REFERENCES patients(id),
                                        CONSTRAINT fk_summary_doctor FOREIGN KEY (doctor_id) REFERENCES doctors(id),
                                        CONSTRAINT fk_summary_visit FOREIGN KEY (last_visit_id) REFERENCES visits(id)
);

CREATE INDEX idx_visits_patient_doctor_start ON visits(patient_id, doctor_id, start_date_time DESC);
CREATE INDEX idx_summary_doctor ON patient_doctor_summary(doctor_id);
