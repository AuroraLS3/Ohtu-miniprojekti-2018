# User guide

## Installation

Download the zip file of the [latest release](https://github.com/Rsl1122/Ohtu-miniprojekti-2018/releases/latest) (the first zip file you find, not the one named _Source code_). To execute the app you must have [Java 8](https://java.com/en/download/) installed in your computer. Unzip the file you downloaded. If using Linux, this can be done simply by using the command:     

```shell
unzip name_of_the_zip_file.zip
```

## Using the app

After unzipping the zip file, the directory generated will contain a [jar](https://en.wikipedia.org/wiki/JAR_(file_format)) file and a subdirectory named _build_, containing a database file prefilled with demo data (to let you quickly get a feeling of how the app works). If you have no use for the demo data, and/or you just want to start fresh with an empty database, just delete the _build_ subdirectory (the app will create a new database automatically).

You can execute the app by typing the following command into your terminal (after [setting your working directory](https://en.wikipedia.org/wiki/Cd_(command)) to the directory you just unzipped, containing the app's jar file):

```shell
java -jar name_of_the_jar_file.jar
```

The app will prompt you to input some commands. The available commands (and corresponding operations) are:
- new: it creates a recommendation (you have to provide the recommendation's type and some information about it)
- all: it lists all existing recommendations
- select: it allows you to select one of the recommendations to see more information about it (requires inputting its type and id when prompted)
- update: it allows you to update an existing recommendation with new information
- delete: it allows you to delete an existing recommendation
- end: it terminates program execution
