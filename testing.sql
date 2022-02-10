insert into patient values (1, '222-22-2222', 'name', '1995-12-12', '123 street', 1);

delete from prescription;
delete from patient;
delete from doctor;

insert into patient (ssn, name, dob, address, doctorid) values ('123-22-2222' , 'bobby benson', '1999-04-04', '12 boo street', 5);

select trade_name from drug where drug_id = 1;

select doctor_id from doctor where name = 'Jany Shreve';
INSERT into pharmacy(phone, name, address) values
  ('5455515018','Kaiser','908 Fermentum Rd.'),
  ('6352496292','CVS','9486 Lectus, Rd.'),
  ('8558409487','Ralphs','518 Montes, Avenue'),
  ('7198541374','Rite Aid','6233 Nunc Street'),
  ('2947915586','Walmart','6199 Ligula. Rd.');

insert into rxprice(pharmacy_id, drug_id, price)
values (1, 1, 1.00),
       (1, 2, 2.00),
       (1, 3, 1.50),
       (1, 4, 3.25),
       (1, 5, 2.99),
       (1, 6, 1.50),
       (1, 7, 2.00),
       (1, 8, 0.99),
       (1, 9, 1.50),
       (1, 10, 0.99),
       (2, 1, 0.99),
       (2, 2, 1.50),
       (2, 3, 1.75),
       (2, 4, 3.00),
       (2, 5, 1.40),
       (2, 6, 1.35),
       (2, 7, 2.23),
       (2, 8, 1.25),
       (2, 9, 1.25),
       (2, 10, 0.89),
       (3, 1, 1.49),
       (3, 2, 1.40),
       (3, 3, 1.65),
       (3, 4, 3.10),
       (3, 5, 1.55),
       (3, 6, 1.65),
       (3, 7, 2.15),
       (3, 8, 1.15),
       (3, 9, 1.49),
       (3, 10, 1.05),
       (4, 1, 1.43),
       (4, 2, 1.43),
       (4, 3, 1.43),
       (4, 4, 3.15),
       (4, 5, 1.52),
       (4, 6, 1.45),
       (4, 7, 2.45),
       (4, 8, 1.22),
       (4, 9, 1.22),
       (4, 10, 1.02),
       (5, 1, 1.45),
       (5, 2, 1.45),
       (5, 3, 1.60),
       (5, 4, 3.00),
       (5, 5, 1.60),
       (5, 6, 1.63),
       (5, 7, 2.10),
       (5, 8, 1.20),
       (5, 9, 1.50),
       (5, 10, 1.08);

select rx_number, p2.name, filled_date, p3.name
from prescription p
join patient p2 on p.patient_id = p2.patient_id
join pharmacy p3 on p.pharmacy_id = p3.pharmacy_id
where drug_id >= 1 and drug_id <= 5;
select price from rxprice where drug_id = 1 and pharmacy_id = 1;
"select price " +
									"from rxprice where drug_id = ? and pharmacy_id = ?