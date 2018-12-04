# User guide

## Installation

Download the zip file containing the jar of the [latest release](https://github.com/Rsl1122/Ohtu-miniprojekti-2018/releases/latest) and a database with demo data. To execute the app you must have [Java 8](https://java.com/en/download/) installed in your computer. You must then unzip the file you downloaded. If using Linux, this can be done simply by using the command:     

```shell
unzip name_of_the_zip_file.zip
```

## Using the app

After unzipping the file, you can execute the program by typing the following command into your terminal (after setting your working directory to the directory containing the app's jar file you unzipped):

```shell
java -jar name_of_the_jar_file.jar
```

The app will prompt you to input some commands. The available commands (and relative operations) are:
- new: it creates a  recommendation (you have to provide the recommendation's type and information about it)
- all: it lists all existing recommendations
- select: it allows you to select one of the recommendations to see more information about it(requires inputting its type and id when prompted)
- update: it allows you to update an existing recommendation with new information
- delete: it allows you to delete an existing recommendation
- end: terminates program execution

After unzipping the zip file, the directory generated will contain a directory named _build_ containing a database file. If you wish to keep the default data don't delete that directory or the database file.

