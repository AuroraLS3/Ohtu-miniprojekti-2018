# User guide

## Installation

Download the jar file of the [latest release](https://github.com/Rsl1122/Ohtu-miniprojekti-2018/releases/latest). To execute the app you must have [Java 8](https://java.com/en/download/) installed in your computer.

## Using the app

Start execution by typing the following command into your terminal (after setting your working directory to the directory containing the app's jar file you just downloaded):

```shell
java -jar name_of_the_jar_file.jar
```

The app will prompt you to input some commands. The available commands (and relative operations) are:
- new: it creates a book recommendation (you have to provide the book's author, title and ISBN code)
- all: it lists all existing book recommendations
- end: terminates program execution

When you execute the app for the first time, a directory named _build_ containing a database file will be generated; if you wish to keep your saved data, don't delete that directory or that database file (and in case you move the jar file to another directory, move the _build_ folder to the same directory).
