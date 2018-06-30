--1. What are the #prods whose name begins with a ’p’ and are less than $300.00?  
select prod_id from `Product` where price < '300' and prod_id like 'p%';
--2. Names of the products stocked in ”d2”.  (a) without in/not in (b) with in/not in  
/* a. */
select pname from `Product`,`Stock` where Product.prod_id = Stock.prod_id and dep_id = 'd2';
/* b. */
select pname from `Product` where prod_id in (select prod_id from `Stock` where dep_id = 'd2');
--3. #prod and names of the products that are out of stock.  (a) without in/not in (b) with in/not in 
/* a. */
select Product.prod_id,pname from `Product`,`Stock` where Product.prod_id = Stock.prod_id and quantity < 0;
/* b. */
select prod_id,pname from `Product` where prod_id in (select prod_id from `Stock` where quantity < '0');
--4. Addresses of the depots where the product ”p1” is stocked.  (a) without exists/not exists and without in/not in (b) with in/not in  (c) with exists/not exists 
/* a. */
select addr from `Depot`,`Stock` where Depot.dep_id = Stock.dep_id and prod_id = 'p1';
/* b. */
select addr from `Depot` where dep_id in (select dep_id from `Stock` where prod_id = 'p1');
/* c. */
select addr from `Depot` where exists (select dep_id from `Stock` where prod_id = 'p1' and dep_id = Depot.dep_id);
--5. #prods whose price is between $250.00 and $400.00.  (a) using intersect. (b) without intersect.  
--Mysql cannot perform ‘intersect’ command.
/* a. */
select prod_id from `Product` where price >=250 intersect select prod_id from `Product` where price <= 400;
/* b. */
select prod_id from `Product` where price >= 250 and price <= 400;
--6. How many products are out of stock?  
select count(distinct prod_id) from `Stock` where quantity < 0;
--7. Average of the prices of the products stocked in the ”d2” depot.  
select avg(price) from `Product` where prod_id in (select prod_id from `Stock` where dep_id = 'd2');
--8. #deps of the depot(s) with the largest capacity (volume). 
select dep_id from `Depot` where volume in (select max(volume) from `Depot`);
--9. Sum of the stocked quantity of each product.  
select prod_id,sum(quantity) from `Stock` group by prod_id;
--10. Products names stocked in at least 3 depots.  (a) using count (b) without using count
/* a. */
select prod_id,pname from `Product` where prod_id in (select prod_id from `Stock` group by prod_id having count(dep_id)>=3);  
/* b. */
select pname from `Product` where prod_id in (select prod_id from `Stock` group by prod_id having sum(case when dep_id is not null then 1 else 0 end)>=3);
--11. #prod stocked in all depots.  (a) using count (b) using exists/not exists  
--MySQL and PostgreSQL cannot perform ‘minus’ command, so I used ‘except’ command on PostgreSQL.
/* a. */
select prod_id from `Stock`,`Depot` group by prod_id having count(distinct Stock.dep_id) = count(distinct Depot.dep_id);
/* b. */
select prod_id from `Product` where not exists (select dep_id from `Depot` except select dep_id from `Stock` where Stock.prod_id = Product.prod_id and dep_id=Stock.dep_id);

