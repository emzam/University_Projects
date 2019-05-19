# Emir Zamwa (emirz), Oblig1 INF3490

import csv
import sys
import time
import itertools
import random
import statistics
import matplotlib.pyplot as plt

# *****************************************************************************

# Main metode
def main():
    global checkLengthCounter;
    # CITIES MUST BE cities E = (1, 24) !!
    cities = 10;
    # IF RUNNING ONCE MAKE SURE TO COMMENT OUT MEAN AND DEVIATION LINES IN
    # GA AND HILL CLIMBING METHOD! OR PROGRAM WILL CRASH.
    runs = 20;
    # Termination criteria for GA loop.
    generations = 300;


    # Comment out which functions you don't want to use between -----

    # --------------------------------------------------------
    # exhaustive_search(cities);
    # print("Amount of times tours were inspected: " +
    #        str(checkLengthCounter));
    # --------------------------------------------------------

    # --------------------------------------------------------
    # run_hill_climber(cities, runs);
    # --------------------------------------------------------

    # --------------------------------------------------------
    # run_genetic_algorithm(cities, 50, generations, runs);
    # print("Amount of times tours were inspected: " +
    #        str(checkLengthCounter));
    # checkLengthCounter = 0;
    # run_genetic_algorithm(cities, 100, generations, runs);
    # print("Amount of times tours were inspected: " +
    #        str(checkLengthCounter));
    # checkLengthCounter = 0;
    # run_genetic_algorithm(cities, 300, generations, runs);
    # print("Amount of times tours were inspected: " +
    #        str(checkLengthCounter));
    # --------------------------------------------------------


    plt.show();

# *****************************************************************************

# Exhaustive Search
def exhaustive_search(xcities):
    print("\n--------- EXHAUSTIVE SEARCH " + str(xcities)+" CITIES ---------");
    print("Running...");

    start_time = time.time();
    permutations =  itertools.permutations(range(xcities));
    currentMinLength = sys.maxsize;
    currentPermutation = None;

    for i in permutations:
        lengthATM = check_length(i);
        if lengthATM < currentMinLength:
            currentMinLength = lengthATM;
            currentPermutation = i;


    end_time = time.time();

    tour = str(currentMinLength);
    perm = str(currentPermutation);
    timee = str(round((end_time - start_time)*1000, 3));

    print("Shortest tour: " + tour + " km");
    print("Permutation: " + perm);
    print("Time: " + timee + " ms");

# *****************************************************************************

# Hill Climbing
def hill_climbing(xcities):
    permutation = random.sample(range(0, xcities), xcities);
    currentPermutation = permutation;
    currentMinLength = check_length(currentPermutation);

    checking = True;

    while checking:
        checking = False;
        allNeighbours = make_neighbours(currentPermutation);

        for i in allNeighbours:
            newPerm = i;
            newLength = check_length(newPerm);

            if newLength < currentMinLength:
                currentMinLength = newLength;
                currentPermutation = newPerm;
                checking = True;

    return(currentPermutation, currentMinLength);

# *****************************************************************************

# Run the hill climbing algorithm
def run_hill_climber(xcities, xnr):
    print("\n--------- HILL CLIMBING " + str(xcities) + " CITIES ---------");
    print("Running " + str(xnr) + " times...");
    resultList = [];
    bestRun = sys.maxsize;
    bestPerm = None;
    worstRun = float('-inf');
    worstPerm = None;
    standardDeviation = None;
    start_time = time.time();

    for i in range(0, xnr):
        trip = hill_climbing(xcities);
        resultList.append(trip);
        perm = trip[0];
        tourLength = trip[1];

        if tourLength < bestRun:
            bestRun = tourLength;
            bestPerm = perm;

        if tourLength > worstRun:
            worstRun = tourLength;
            worstPerm = perm;

    meanRun = sum([result[1] for result in resultList])/len(resultList);
    stdDev = statistics.stdev([result[1] for result in resultList]);
    end_time = time.time();
    print("Best tour length: " + str(bestRun) + " km");
    print("Permutation for best run: " + str(bestPerm));
    print("Worst tour length: " + str(worstRun) + " km");
    print("Permutation for worst run: " + str(worstPerm));
    print("Mean for all runs: " + str(round(meanRun, 3)) + " km");
    print("Standard deviation for all runs: " + str(round(stdDev, 3)));
    print("Time: " + str(round((end_time - start_time)*1000, 3)) + " ms");

# *****************************************************************************

# The genetic algoritm
def genetic_algorithm(xcities, popAmount, generations):
    # Tuples with [[individual, fitness], ...]
    population = [];
    ind = [None, sys.maxsize];
    individuals = [];
    generation = 0;

    # Making new population with set amount of individuals and cities.
    # Their "fitness" is the tour length.
    for i in range(0, popAmount):
        individual = random.sample(range(0, xcities), xcities);
        lengthOfIndividual = round(check_length(individual), 3);
        population.append([individual, lengthOfIndividual]);

    newPopulation = population;

    while True:
        parents = select_parents(newPopulation);
        parentPairs = recombine_parents(parents);
        children = make_children(parentPairs);
        generation += 1;
        mutatedChildren = mutate_children(children);
        for i in range(0, len(mutatedChildren)):
            newPopulation.append(mutatedChildren[i]);

        newPopulation.sort(key=lambda e: e[1]);
        newPopulation = newPopulation[:popAmount-1]
        ind = best_individual(newPopulation);
        individuals.append(ind);
        if(generation == generations):
            break;

    return individuals, ind;

# *****************************************************************************
# Run the hill climbing algorithm
def run_genetic_algorithm(xcities, popAmount, generations, runs):
    print("\n--------- GENETIC ALGORITHM " + str(xcities)+" CITIES ---------");
    print("Running " + str(runs) + " times...");
    resultList = [];
    listOfindividuals = [];
    bestRun = sys.maxsize;
    bestPerm = None;
    worstRun = float('-inf');
    worstPerm = None;
    standardDeviation = None;
    start_time = time.time();


    for i in range(0, runs):
        individual = genetic_algorithm(xcities, popAmount, generations);
        resultList.append(individual[1]);
        listOfindividuals.append(individual[0]);
        perm = individual[1][0];
        tourLength = individual[1][1];

        if tourLength < bestRun:
            bestRun = tourLength;
            bestPerm = perm;

        if tourLength > worstRun:
            worstRun = tourLength;
            worstPerm = perm;

    # Individual list now contains indexes of length xRuns. Each index has
    # best fit individual in each generation. Generation is their index from
    # 0 to xGenerations-1
    averageFitnessAcrossRuns = [];
    for i in range(0, generations):
        number = 0;
        for j in range(0, runs):
            number += listOfindividuals[j][i][1];
        averageFitness = number/runs;
        averageFitnessAcrossRuns.append(round(averageFitness, 3));

    text = "Population of " + str(popAmount);
    plt.plot(averageFitnessAcrossRuns, label=text);
    plt.xlabel('Generations');
    plt.ylabel('Fitness as tour length');
    plt.grid();
    plt.legend();

    meanRun = sum([result[1] for result in resultList])/len(resultList);
    stdDev = statistics.stdev([result[1] for result in resultList]);
    end_time = time.time();
    print("Best tour length: " + str(bestRun) + " km");
    print("Permutation for best run: " + str(bestPerm));
    print("Worst tour length: " + str(worstRun) + " km");
    print("Permutation for worst run: " + str(worstPerm));
    print("Mean for all runs: " + str(round(meanRun, 3)) + " km");
    print("Standard deviation for all runs: " + str(round(stdDev, 3)));
    print("Time: " + str(round((end_time - start_time)*1000, 3)) + " ms");

# *****************************************************************************

# Returns best in a list
def best_individual(population):
    length = sys.maxsize;
    ind = None;

    for i in population:
        temp = i[1];
        if temp < length:
            length = temp;
            ind = i;

    return ind;
# *****************************************************************************

# This method mutates the children made. Each child has a propability of
# getting mutated. Not every child is mutated.
def mutate_children(children):
    kidCopy = list(children);
    mutationChance = 0.3;

    for i in kidCopy:
        rollTheDice = random.random();
        if rollTheDice < mutationChance:
            one, two = random.sample(range(0, len(i)), 2);
            temp = i[one];
            i[one] = i[two];
            i[two] = temp;

    fitnessGiven = [];
    for i in kidCopy:
        fitnessGiven.append([i, check_length(i)]);

    return fitnessGiven;

# *****************************************************************************

# The genetic algorithm loop
def make_children(parents):
    offspring = [];

    for i in parents:
        child = ordered_crossover(i[0], i[1]);
        offspring.append(child);

    return offspring;

# *****************************************************************************

# Mutation: ordered crossover
def ordered_crossover(parent1, parent2):
    cities = len(parent1[0]);
    firstIndex = None;
    lastIndex = None;

    if cities <= 2:
        firstIndex = 0; lastIndex = 0;
    elif cities == 3:
        firstIndex = 0; lastIndex = 1;
    else:
        # Random formula for selecting subset ca in middle of tour
        firstIndex = int(cities/2)-1;
        lastIndex = int(firstIndex/2) + int(cities/2);

    sequence = [];
    child = [None]*cities;

    for i in range(firstIndex, lastIndex+1):
        child[i] = parent1[0][i];
        sequence.append(parent1[0][i]);

    i = lastIndex;
    secondi = lastIndex;
    third = -1;
    loopChecker = cities;

    while True:
        i += 1;
        if child[i] == None:
            k = third;
            while True:
                k += 1;
                temp = parent2[0][k];
                if not ordered_check(temp, sequence):
                    child[i] = temp;
                    third = k;
                    break;
                if k == cities-1:
                    k = -1;

        if i == (cities-1):
            i = -1;
        newCheck = 0;
        for h in range(0, cities):
            if child[h] != None:
                newCheck += 1;
        if (loopChecker - newCheck) == 0:
            break;


    return child;

# *****************************************************************************
# Checking if number contains in child
def ordered_check(number, sequence):
    check = False;

    for i in range(0, len(sequence)):
        if sequence[i] == number:
            check = True;

    return check;

# *****************************************************************************

# This method will randomly select the population/2 parents.
# Two and two parents will randomly get in paris. Also making sure that paris
# don't have the same permutation.
def select_parents(population):
    popCopy = list(population);
    parents = 0;

    if int(len(popCopy)/2)%2 == 0:
        parents = int(len(popCopy)/2);
    else:
        parents = int(len(popCopy)/2) + 1;

    parentSet1 = [];
    parentSet2 = [];
    selectedParents = [];

    for i in range(0, parents):
        temp = random.choice(popCopy);
        while True:
            if temp[0] not in selectedParents:
                selectedParents.append(temp);
                break;

    return selectedParents;

# *****************************************************************************

# Recombining parents into paris
def recombine_parents(parents):
    xPairs = len(parents);
    pairs = [];

    i = 0;
    while i < xPairs-1:
        pairs.append([parents[i], parents[i+1]]);
        i += 2;

    return pairs;

# *****************************************************************************

# Generate all neighbours
def make_neighbours(permutation):
    permLength = len(permutation);
    newList = [permutation];

    for i in range(0, permLength):
        for j in range(i, permLength):
            if j != i:
                newList.append(swap_neighbours(permutation, i, j));

    return newList;

# *****************************************************************************

# Swap neighbour
def swap_neighbours(permutation, i, j):
    permLength = len(permutation);
    newList = list(permutation);

    temp = newList[i];
    newList[i] = newList[j];
    newList[j] = temp;

    return newList;

# *****************************************************************************

# Check length of a trip
def check_length(permutation):
    permLength = len(permutation);
    length = 0;

    for i in range(0, permLength-1):
        # Added +1 to row to make sure I don't include city name.
        row = permutation[i]+1;
        column = permutation[i+1];
        length += float(data[row][column]);

        # When loop is on last city, make sure to bring tour back to 1st city
        if i == permLength-2:
            length += float(data[column+1][permutation[0]]);

    global checkLengthCounter;
    checkLengthCounter += 1;
    return round(length, 2);

# *****************************************************************************

# Global variable "data" read from file. Running main() after reading.
with open("european_cities.csv", "r") as f:
    data = list(csv.reader(f, delimiter=';'))

checkLengthCounter = 0;
main();
