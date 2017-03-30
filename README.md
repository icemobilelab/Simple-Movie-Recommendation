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

The input must be a CSV file containing the columns:
 - userId: the ID of the user
 - productId: the ID of the product
 - userRate: The rate given by this user to this product (0 if not rated)
 
Example:
```
1,15,7
6,16,9
0,10,8
9,9,0
```

### Output

The generated output is going to be a CSV file, with the following columns:
 - userId: the ID of the user
 - productId: the ID of the product
 - userRate: The rate given by this user to this product (0 if not rated)
 - predictedRate: The predicted rate for this product to this user
 
The predict rate is how much every user would rate that specific product. Example:

```
1,15,7,8.57
6,16,9,7.521
0,10,8,9.512
9,9,0,4.123
```

Then it's possible to filter, for every user, the products with zero rate and, later, rank
them in descend order of predicted rate. This rank are the recommended movies.

