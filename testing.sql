insert into patient values (1, '222-22-2222', 'name', '1995-12-12', '123 street', 1);

delete from patient;
delete from doctor;
delete from prescription;

insert into prescription (doctor_id, patient_id, drug_id, quantity, filled_date, pharmacy_id) VALUES
(12, 12, 31)

select trade_name from drug where drug_id = 1;