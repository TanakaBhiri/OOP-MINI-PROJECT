# Lab 11 — Library Management System mini-project

Implements an abstract `LibraryItem`, `Book` and `Dvd`, `Borrowable`, `Member`, a `Loan` record, custom exceptions, maps keyed by IDs, a three-loan limit, title search with streams, overdue listing, and CSV persistence.

## Compile and run
```bash
javac *.java
java Library
```
Run from this folder. Data is saved to `library-items.csv` and `library-members.csv` when menu option `0` is selected. The simple CSV format assumes titles and names do not contain commas. Books have 21-day loans; DVDs have 7-day loans.
