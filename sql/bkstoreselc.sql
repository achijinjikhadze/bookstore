select books.title, (authors.firstname + ' ' + authors.lastname) as author, books.price, books.quantity
from books inner join authors  on books.authorid = authors.authorid
