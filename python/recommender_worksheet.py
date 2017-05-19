#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
"""

#-----------------------------------------------------
# Data import
#-----------------------------------------------------

import pandas as pd
import numpy as np
FakeRatings=False

# Read 'file' into a Pandas dataframe
ratings_file = '''"Timestamp","Username","Star Wars: The Force Awakens (2015)","Mad Max: Fury Road (2015)","Deadpool (2016)","The Martian (2015)","Avengers: Age of Ultron (2015)","The Revenant (2015)","Batman v Superman: Dawn of Justice (2016)","Jurassic World (2015)","Inside Out (2015)","Captain America: Civil War (2016)","Suicide Squad (2016)","Ant-Man (2015)","The Hateful Eight (2015)","Spectre (2015)","Furious 7 (2015)","Arrival (II) (2016)","Zootopia (2016)","Stranger Things (2016)","Star Wars Rogue One (2016)","X-Men: Apocalypse (2016)"
"2017/05/02 12:11:03 PM GMT+2","ricardo.atsouza@gmail.com","0","0","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10","10"
"2017/05/17 5:01:12 PM GMT+2","Test@test.com","5","3","7","2","10","7","7","6","6","6","5","4","3","7","7","5","5","2","9","7"'''

lines = ratings_file.splitlines()
movies = lines[0].split(',')[2:]
data= [d.split(',') for d in lines]

def remove_quotes(x): return x.replace('"', '')

df=pd.DataFrame(data[1:], columns=list(map(remove_quotes,data[0])))
df=df.applymap(remove_quotes)

# Create ratings array
#Drop columns:
user_movie_matrix = df.drop(['Timestamp','Username'], axis=1)

#Convert strings to numbers:
user_movie_matrix = user_movie_matrix.applymap(lambda x: int(x))

#Create numpy array of ratings:
ratings = user_movie_matrix.values.T
ratings.shape

#Create numpy array of users
users= df['Username'].values
movies=df.columns[2:].values

number_of_users=len(users)
number_of_movies=len(movies)

if FakeRatings:
    num_movies = 10                      
    num_users =   3                    
    ratings = np.random.randint(5, size = (num_movies, num_users))
    users=list(range(num_users))
    movies=list(range(num_movies))
    print (ratings)


(num_movies, num_users)= ratings.shape

print('number of movies:', num_movies)
print('number of users :', num_users)

original_ratings=ratings
#-----------------------------------------------------
# Normalization
#-----------------------------------------------------

did_rate = (ratings != 0) * 1

print (ratings)
print (did_rate)

def normalize_ratings(ratings, did_rate):
    """ a function that normalizes a dataset
    """
    num_movies = ratings.shape[0]
    
    ratings_mean = np.zeros(shape = (num_movies, 1))
    ratings_norm = np.zeros(shape = ratings.shape)
    
    for i in range(num_movies): 
        # Get all the indexes where there is a 1
        idx = np.where(did_rate[i] == 1)[0]
        #  Calculate mean rating of ith movie only from user's that gave a rating
        ratings_mean[i] = np.mean(ratings[i, idx])
        ratings_norm[i, idx] = ratings[i, idx] - ratings_mean[i]
    
    return ratings_norm, ratings_mean

# Normalize ratings
ratings, ratings_mean = normalize_ratings(ratings, did_rate)



#-----------------------------------------------------
# Prepare Gradient desccent
#-----------------------------------------------------
# parameters:
num_features = 10
# Initialize Parameters theta (user_prefs), X (movie_features)
movie_features = np.random.randn( num_movies, num_features )
user_prefs = np.random.randn( num_users, num_features )
# old code:
#initial_X_and_theta = np.r_[movie_features.T.flatten(), user_prefs.T.flatten()]
# better code:
initial_X_and_theta = np.concatenate((movie_features.T.flatten(), user_prefs.T.flatten()))


print (movie_features)
print (user_prefs)
print (initial_X_and_theta)

print(initial_X_and_theta.shape)
print(movie_features.T.flatten().shape)
print(user_prefs.T.flatten().shape)



# helper function:
def unroll_params(X_and_theta, num_users, num_movies, num_features):
    # Retrieve the X and theta matrixes from X_and_theta, based on their dimensions (num_features, num_movies, num_movies)
    # --------------------------------------------------------------------------------------------------------------
    # Get the first 30 (10 * 3) rows in the 48 X 1 column vector
    first_30 = X_and_theta[:num_movies * num_features]
    # Reshape this column vector into a 10 X 3 matrix
    X = first_30.reshape((num_features, num_movies)).transpose()
    # Get the rest of the 18 the numbers, after the first 30
    last_18 = X_and_theta[num_movies * num_features:]
    # Reshape this column vector into a 6 X 3 matrix
    theta = last_18.reshape(num_features, num_users ).transpose()
    #print(theta)
    return X, theta

# test:
# x,t= unroll_params(initial_X_and_theta, num_users, num_movies, num_features)

def calculate_gradient(X_and_theta, ratings, did_rate, num_users, num_movies, num_features, reg_param):
	X, theta = unroll_params(X_and_theta, num_users, num_movies, num_features)
	
	# we multiply by did_rate because we only want to consider observations for which a rating was given
	difference = X.dot( theta.T ) * did_rate - ratings
	X_grad = difference.dot( theta ) + reg_param * X
	theta_grad = difference.T.dot( X ) + reg_param * theta
	# wrap the gradients back into a column vector 
	return np.r_[X_grad.T.flatten(), theta_grad.T.flatten()]

def calculate_cost(X_and_theta, ratings, did_rate, num_users, num_movies, num_features, reg_param):
    X, theta = unroll_params(X_and_theta, num_users, num_movies, num_features)

    # we multiply (element-wise) by did_rate because we only want to consider observations for which a rating was given
    cost = sum( (X.dot( theta.T ) * did_rate - ratings) ** 2 ) / 2
    #  print(cost)
    # '**' means an element-wise power
    regularization = (reg_param / 2) * (sum( theta**2 ) + sum(X**2))
    return cost + regularization
 

# import these for advanced optimizations (like gradient descent)

from scipy import optimize
from numpy import *            # bug fix 



#-----------------------------------------------------
# Run Gradient descent
#-----------------------------------------------------
# regularization parameter
reg_param = 30

# perform gradient descent, find the minimum cost (sum of squared errors) and optimal values of X (movie_features) and Theta (user_prefs)
minimized_cost_and_optimal_params = optimize.fmin_cg(calculate_cost, fprime=calculate_gradient, x0=initial_X_and_theta,
                                                     args=(ratings, did_rate, num_users, num_movies, num_features, reg_param), 								
                                                     maxiter=1000, disp=True, full_output=True ) 

cost, optimal_movie_features_and_user_prefs = minimized_cost_and_optimal_params[1], minimized_cost_and_optimal_params[0]


# unroll once again
movie_features, user_prefs = unroll_params(optimal_movie_features_and_user_prefs, num_users, num_movies, num_features)

print (movie_features)
print (user_prefs)


movie_features.shape
user_prefs.shape


#-----------------------------------------------------
# Make some predictions (movie recommendations). Dot product
#-----------------------------------------------------

all_predictions = movie_features.dot( user_prefs.T )

print (all_predictions)


predictions_for_me = all_predictions[:, 0:1] + ratings_mean         # todo: parametrize
print(original_ratings[:,0:1])
print(ratings_mean)


r = np.argsort(predictions_for_me.flatten())

print ('Top recommendations for you:')
for i in  range(0, min(len(r),20)-1 ):
    print ('Predicting rating ', '%.2f' % predictions_for_me[r[-i]], 'for movie ', movies[r[-i]])



#-----------------------------------------------------
# scratch
#-----------------------------------------------------

all_predictions[:,0].shape
all_predictions[:,0:1].shape

ratings_mean.shape
# 100 movies, 3000 users
