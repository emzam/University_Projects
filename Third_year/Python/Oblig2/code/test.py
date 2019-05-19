# Emir Zamwa (emirz), Oblig2 INF3490
'''
    This file was used to set in values as used in the lectures to check
    if i got correct values out. Was useful to find lot of mistakes and bugs.
    Tested with and without sigmoid. Without sigmoid resulted in the end
    with correct values compared to lectures. Tested with sigmoid, corrected
    a few mistakes here and there, and then got correct with sigmoid too.
    Changed the fails in the original mlp file as i found them here in the
    test file.
'''

import numpy as np
import random
import copy

# ****************************************************************************
class test:
    def __init__(self, inputs, targets, nhidden):
        self.beta = 1; self.eta = 0.1; self.momentum = 0.0
        self.inputs = inputs
        self.input_weights = []
        self.hidden_weights = []
        self.nhidden = nhidden
        self.target = targets


        for i in range(0, len(self.inputs) + 1):
            self.input_weights.append([])
            self.hidden_weights.append([])
        self.input_weights[0].append(-1)
        self.input_weights[0].append(0)
        self.input_weights[1].append(0)
        self.input_weights[1].append(1)
        self.input_weights[2].append(1)
        self.input_weights[2].append(1)

        self.hidden_weights[0].append(1)
        self.hidden_weights[0].append(-1)
        self.hidden_weights[1].append(0)
        self.hidden_weights[1].append(1)
        self.hidden_weights[2].append(1)
        self.hidden_weights[2].append(1)

        self.hidden_activations = np.zeros(2)
        self.output_activations = np.zeros(2)
        self.train(self.inputs, self.target)
        #self.forward(self.inputs)
    # ****************************************************************************

    # The main training method
    def train(self, inputs, targets, iterations=1):
        for iter in range(0, iterations):
            inputKopi, hiddenKopi = self.forward(inputs)
            output_deltas = [0] * 2
            hidden_deltas = [0] * self.nhidden
            old_hidden_weights = copy.deepcopy(self.hidden_weights)
            old_input_weights = copy.deepcopy(self.input_weights)

            # Calculating delta values for outputs in the first for loop.
            # in addition, also updating hidden weights with new ones
            for i in range(0, 2):
                delta = ((self.output_activations[i] - targets[i]) * \
                    self.output_activations[i]*(1-self.output_activations[i]))
                output_deltas[i] = delta
                for j in range(0, self.nhidden + 1):
                    temp = self.hidden_weights[j][i]
                    updated_weight = (temp - \
                        (self.eta * output_deltas[i] * hiddenKopi[j]))
                    self.hidden_weights[j][i] = updated_weight

            print(output_deltas)

            # Calculating deltas values for hidden layer
            for i in range(0, self.nhidden):
                delta = 0
                for j in range(0, 2):
                    delta += output_deltas[j] * old_hidden_weights[i][j]
                hidden_deltas[i] = (delta * \
                    self.hidden_activations[i]*(1-self.hidden_activations[i]))

            print(hidden_deltas)

            # Updating input weights
            for i in range(0, self.nhidden):
                for j in range(0, len(inputKopi)):
                    temp = self.input_weights[j][i]
                    updated_weight = (temp - \
                        (self.eta * hidden_deltas[i] * inputKopi[j]))
                    self.input_weights[j][i] = updated_weight

        print("New hiddens: " + str(self.hidden_activations))
        print("New outputs: " + str(self.output_activations))
        print("New inputW: " + str(self.input_weights))
        print("New hiddenW: " + str(self.hidden_weights))
        print("")

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
        inputKopi = np.append(inputs, self.beta)
        hiddenKopi = np.append(self.hidden_activations, self.beta)

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
        for i in range(0, 2):
            value = 0.0
            for j in range(0, self.nhidden + 1):
                value += hiddenKopi[j] * self.hidden_weights[j][i]
            self.output_activations[i] = value
        # Sigmoid
        print("HEI" + str(self.output_activations))
        for i in range(0, 2):
            self.output_activations[i] = (\
                1/(1 + np.exp(-self.beta * self.output_activations[i])))

        print("Inputs: " + str(self.inputs))
        print("Hiddens: " + str(self.hidden_activations))
        print("Outputs: " + str(self.output_activations))
        print("InputW: " + str(self.input_weights))
        print("HiddenW: " + str(self.hidden_weights))
        print("")

        return inputKopi, hiddenKopi

def main():
    inputs = [0, 1]
    target = [1, 0]
    test(inputs, target, 2)

main()
