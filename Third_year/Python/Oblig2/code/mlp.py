# Emir Zamwa (emirz), Oblig2 INF3490

import numpy as np
import random
import time
import copy
import pandas as pd

# *****************************************************************************
class mlp:
    def __init__(self, inputs, targets, nhidden):
        self.beta = 1; self.eta = 0.1; self.bias = 1
        self.input_weights = []; self.hidden_weights = []
        self.hidden_activations = np.zeros(nhidden)
        self.output_activations = np.zeros(8)
        self.nhidden = nhidden

        self.make_initial_weights(inputs[0])

# *****************************************************************************

    # This methdo initializes the weights of the system
    def make_initial_weights(self, inputs):
        # Giving the input random weights between -0,5 and 0,5
        for i in range(0, len(inputs) + 1):
            self.input_weights.append([])
            for j in range(0, self.nhidden):
                rand = random.uniform(-0.5, 0.5)
                self.input_weights[i].append(rand)

        # Also giving random weights for the hidden nodes
        for i in range(0, self.nhidden + 1):
            self.hidden_weights.append([])
            for j in range(0, 8):
                rand = random.uniform(-0.5, 0.5)
                self.hidden_weights[i].append(rand)

# *****************************************************************************

    # Function to stop overfitting
    def earlystopping(self, inputs, targets, valid, validtargets):
        start_time = time.time();
        prev_success_rate = 0
        print("Running...")
        ep = 0
        test = 0
        while True:
            counter = 0
            self.train(inputs, targets)

            for i in range(0, len(valid)):
                self.forward(valid[i])
                cv = self.clamp_values(self.output_activations)
                if(cv == validtargets[i]).all():
                    counter += 1;

            success_rate = counter/len(validtargets)

            # If more chances to get better is alloved to be used, change
            # stop to a number >= 1
            stop = 1
            if prev_success_rate > success_rate:
                test += 1
                print("Didn't find better [" + str(test) + " out of "+ str(stop)+"]")
                if test == stop:
                    break;

            ep += 1;
            prev_success_rate = success_rate
            end_time = time.time();


            print("Epoch[" + str(ep) + "] success rate ≈ " +
                                    str(round(success_rate*100, 2)) + "%")

        timee = str(round((end_time - start_time)*1, 3));
        print("Time used: " + timee + " seconds\n");



# *****************************************************************************

    # The main training method
    def train(self, inputs, targets, iterations=5):
        for iter in range(0, iterations):
            # Shuffling the list set per run
            X = list(zip(inputs, targets))
            random.shuffle(X)
            inputs, targets = zip(*X)
            for counter in range(0, len(inputs)):
                inputKopi, hiddenKopi = self.forward(inputs[counter])
                output_deltas = [0] * 8
                hidden_deltas = [0] * self.nhidden
                old_hidden_weights = copy.deepcopy(self.hidden_weights)
                old_input_weights = copy.deepcopy(self.input_weights)

                # Calculating delta values for outputs in the first for loop.
                # in addition, also updating hidden weights with new ones
                for i in range(0, 8):
                    delta = ((self.output_activations[i] - targets[counter][i]) * \
                        self.output_activations[i]*(1-self.output_activations[i]))
                    output_deltas[i] = delta
                    for j in range(0, self.nhidden + 1):
                        temp = self.hidden_weights[j][i]
                        updated_weight = (temp - \
                            (self.eta * output_deltas[i] * hiddenKopi[j]))
                        self.hidden_weights[j][i] = updated_weight

                # Calculating deltas values for hidden layer
                for i in range(0, self.nhidden):
                    delta = 0
                    for j in range(0, 8):
                        delta += output_deltas[j] * old_hidden_weights[i][j]
                    hidden_deltas[i] = (delta * \
                        self.hidden_activations[i]*(1-self.hidden_activations[i]))

                # Updating input weights
                for i in range(0, self.nhidden):
                    for j in range(0, len(inputKopi)):
                        temp = self.input_weights[j][i]
                        updated_weight = (temp - \
                            (self.eta * hidden_deltas[i] * inputKopi[j]))
                        self.input_weights[j][i] = updated_weight

# *****************************************************************************

    # This function clampes the list
    def clamp_values(self, outputs):
        max_value = max(outputs)
        clamped = np.zeros(len(outputs))

        for i in range(0, len(outputs)):
            if outputs[i] == max_value:
                clamped[i] = 1.0
                break;

        return clamped

# *****************************************************************************

    '''
    This method goes from left to right of the system, and calculates the
    values of each node in the hidden layer, and also the values of the output.
    By values I mean activations.
    '''
    def forward(self, inputs):
        # Making copies of lists and adding the bias so it is included in
        # calculations of values. Thats why each for-loop goes to + 1.
        inputKopi = np.append(inputs, self.bias)
        hiddenKopi = np.append(self.hidden_activations, self.bias)

        for i in range(0, self.nhidden):
            value = 0.0
            for j in range(0, len(inputs) + 1):
                # Using the formula from slides to give values to hidden nodes
                value += inputKopi[j] * self.input_weights[j][i]
            self.hidden_activations[i] = value
        # Sigmoid
        for i in range(0, self.nhidden):
            self.hidden_activations[i] = (\
                1/(1 + np.exp(-self.beta * self.hidden_activations[i])))
            hiddenKopi[i] = self.hidden_activations[i]

        # Same principle as above, but with the outputs
        for i in range(0, 8):
            value = 0.0
            for j in range(0, self.nhidden + 1):
                value += hiddenKopi[j] * self.hidden_weights[j][i]
            self.output_activations[i] = value
        # Sigmoid
        for i in range(0, 8):
            self.output_activations[i] = (\
                1/(1 + np.exp(-self.beta * self.output_activations[i])))

        return inputKopi, hiddenKopi

# *****************************************************************************

    # Printing out the confusion matrix
    def confusion(self, inputs, targets):
        matrix = []
        xy = ['a, b, c, d, e, f, g, h']

        for i in range(0, 8):
            matrix.append([])
            for j in range(0, 8):
                matrix[i].append(0)

        for i in range(0, len(inputs)):
            self.forward(inputs[i])
            output = self.clamp_values(self.output_activations)

            actual = 0; predicted = 0
            for j in range(0, 8):
                if output[j] == 1:
                    actual = j
                if targets[i][j] == 1:
                    predicted = j
            matrix[actual][predicted] += 1

        print("CONFUSION MATRIX")
        print("Actual = Rows\nPredicted = Columns")

        # IF PANDAS CANNOT BE USED, PRINT THIS AND COMMENT OUT UNDER
        # for i in range(0, len(matrix)):
        #     print(matrix[i])

        # IF PANDAS CANNOT BE USED, COMMENT OUT THIS AND PRINT ABOVE
        # ALSO COMMENT OUT THE 'import pandas as pd' TOP OF THE CODE
        print(pd.DataFrame(matrix, index=['a','b','c','d', 'e','f','g','h'],\
                                   columns=['a','b','c','d', 'e','f','g','h']))
