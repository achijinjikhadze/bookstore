-- avtorebi
create table authors (
    authorid int primary key identity(1,1),
    firstname nvarchar(100),
    lastname nvarchar(100),
    bio nvarchar(max),
    birthdate date
);

-- categoriebi
create table categories (
    categoryid int primary key identity(1,1),
    categoryname nvarchar(100)
);

--wignebi
create table books (
    bookid int primary key identity(1,1),
    title nvarchar(255),
    authorid int, 
    publicationdate date,
    price decimal(5, 2),
	getprice decimal(5,2),
    categoryid int, 
    quantity int, 
    descript nvarchar(max),
    foreign key (authorid) references authors(authorid),
    foreign key (categoryid) references categories(categoryid)
);

-- customers
create table customers (
    customerid int primary key identity(1,1),
    firstname nvarchar(100),
    lastname nvarchar(100),
    email nvarchar(100),
    password nvarchar(100),
    phone nvarchar(15),
    address nvarchar(300),
    registrationdate date,
    role nvarchar(20) default 'user'
);


-- orders
create table orders (
    orderid int primary key identity(1,1),
    customerid int,
    orderdate date,
    orderaddress nvarchar(300),
    orderprice decimal(10, 2), 
    foreign key (customerid) references customers(customerid)
);


-- order details 
create table order_details (
    orderdetailsid int primary key identity(1,1),
    orderid int ,
    bookid int,
    quantity int,
    --oneprice
    --totalprice
    foreign key (orderid) references orders(orderid),
    foreign key (bookid) references books(bookid)
);


-- reviews
create table reviews (
    reviewid int primary key identity(1,1),
    customerid int,
    bookid int,
    rating int check (rating between 1 and 5),
    foreign key (customerid) references customers(customerid),
    foreign key (bookid) references books(bookid)  
);

