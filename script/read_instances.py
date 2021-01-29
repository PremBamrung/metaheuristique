from os import listdir
from os.path import isfile, join
import numpy as np

# Get the instances path
# Change the path if neeeded
path = "/home/prembamrung/Documents/Valdom/tp-ppc/JSPLIB/instances"


def get_numeric(line):
    """Return only number from a list

    Args:
        line (list): list of string

    Returns:
        list: list of string with only number
    """
    result = []
    for l in line:
        if l.isnumeric():
            result.append(str(l))
    return result


def read_instance(file):
    """Read an instance and return a data matrix

    Args:
        file (instance file): opened instance

    Returns:
        [np.ndarray]: [np array of the value inside the instance file]
    """
    with open(file) as fp:
        lines = fp.readlines()
        
    if lines[0][0] == "#":
        if lines[0][1]=='+':
            lines = lines[5:]
        else :
            lines = lines[2:]
            
    else:
        lines = lines[2:]

    data = []

    for line in lines:
        toto = line.strip().split(" ")
        data.append(get_numeric(toto))

    data = np.array(data)
    return data


def get_files(path):
    """Get all the instance files from a directory

    Args:
        path (str): path of all the instances

    Returns:
        files (list): list of all direct path of the instance file
    """
    files = [f for f in listdir(path) if isfile(join(path, f))]
    files = [path + "/" + file for file in files]
    return files


def get_machines(data):
    """Get all the machines used in an instance data matrix

    Args:
        data (np.ndarray): np array of an instance opened with read_instance()

    Returns:
        np.ndarray: np array with only the machines used
    """
    machines = np.zeros((len(data[:][:]), int(len(data[0]) / 2)), dtype=int)
    for c, i in enumerate(data):
        for index, j in enumerate(i):
            if index % 2 == 0:
                machines[int(c)][int(index / 2)] = int(j)
    return np.array(machines, dtype=int)


def get_duration(data):
    """Get all durations used in an instance data matrix

    Args:
        data (np.ndarray): np array of an instance opeend with read_instance()

    Returns:
        np.ndarray: np array with only the durations
    """
    durations = np.zeros((len(data[:][:]), int(len(data[0]) / 2)), dtype=int)
    for c, i in enumerate(data):
        for index, j in enumerate(i):
            if index % 2 == 1:
                durations[int(c)][int(index / 2)] = int(j)
    return np.array(durations, dtype=int)


if __name__ == "__main__":
    # Change the path if neeeded
    path = "/home/prembamrung/Documents/Valdom/tp-ppc/JSPLIB/instances"
    # Get all the files name from the path
    files = get_files(path)
    for f in files:
        # read one instance and output a data matrix
        data = read_instance(f)
        print(type(data) == np.ndarray)
        # Create machine and duration matrixes here on the varible data
        machines = get_machines(data)
        duration = get_duration(data)
        # apply model

