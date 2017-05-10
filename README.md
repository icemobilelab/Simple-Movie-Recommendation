# Simple Movie Recommendation

This project was created for the Open Day at Ice Mobile. Its goal is to show how a simple
movie recommendation works by using the algorithm of Collaborative Filtering.

## Compile

`sbt compile`

## To create a jar file

The jar file will be located in `target/scala-2.11/recommender.jar`. Just type the following command:

`sbt assembly`

## Run

After creating the jar file and type:

`java -jar target/scala-2.11/recommender.jar <input> <output>`

Or, to run from the source code, type:

`stb "run <input> <output>"`

Examples of input and output files can be found in this project.

### Input

The input must be a CSV file with header, where:
 - The first column contains a timestamp*
 - The second the user name (userId or user email)
 - All the other columns contains the rating of the movies
 
Example:
```
"Timestamp","Username","Movie title A", "Movie title B"
"2017/05/02 12:11:03 PM", "user1.email@gmail.com", "4","9"
"2017/05/02 12:13:03 PM", "user2.email@gmail.com", "2","1"
"2017/05/02 12:15:03 PM", "user3.email@gmail.com", "1","10"
```

* Note: The first columns (timestamp) is not actually being used for anything in the recommender. Therefore, you can use it for an
internal reference

### Output

The generated output is going to be a CSV file, with the same columns from the input file (except the timestamp one):

Example:
```
"Username","Movie title A", "Movie title B"
"user1.email@gmail.com", "5","7"
"user2.email@gmail.com", "1",".5"
"user3.email@gmail.com", "2","8"
```

