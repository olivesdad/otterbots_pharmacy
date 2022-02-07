insert into patient values (1, '222-22-2222', 'name', '1995-12-12', '123 street', 1);

delete from prescription;
delete from patient;
delete from doctor;

insert into patient (ssn, name, dob, address, doctorid) values ('123-22-2222' , 'bobby benson', '1999-04-04', '12 boo street', 5);

select trade_name from drug where drug_id = 1;

select doctor_id from doctor where name = 'Jany Shreve';

insert into pharmcompany (name, phone) values ('suzy drew', '7148514133');