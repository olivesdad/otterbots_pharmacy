SELECT r.drugtradename, r.price, p.pharmacyphone
FROM prescription p
         JOIN drug d on p.drugtradename = d.drugtradename
         JOIN rxprice r on d.drugtradename = r.drugtradename
where p.filleddate is not null
ORDER BY r.drugtradename;


INSERT into doctor
values('222-22-2222', 'dr bob', 'feet', '2021-01-01' );
INSERT into doctor
values('111-22-2222', 'dr joe', 'finger nails', '2011-03-01' );

INSERT INTO patient
VALUES ('124-24-5643', 'King Drew', '1983-11-19', '123 cool street', '222-22-2222' );
INSERT INTO patient
VALUES ('124-24-1111', 'Princess Olive', '2015-04-15', '123 cool street', '222-22-2222' );

insert into pharmcompany
values ('thrifty', '1234567892');
insert into drug
values ('Green Tea Icecream', 'green tea and sugar', 'thrifty');
insert into pharmacy
values ('5555551234', 'stree drugs', 'its the street foo');

insert into rxprice
values ('5555551234', 'Green Tea Icecream', 40.55);

insert into prescription (patientssn, doctorssn, drugtradename, quantity, pharmacyphone, filleddate)
values ('124-24-5643', '222-22-2222', 'Green Tea Icecream', 50, '5555551234','2021-01-24');
insert into prescription (patientssn, doctorssn, drugtradename, quantity)
values ('124-24-1111', '222-22-2222', 'Green Tea Icecream', 50 );

update prescription
set filleddate = '2022-01-01', pharmacyphone = '5555551234'
where rxnumber = 2;

