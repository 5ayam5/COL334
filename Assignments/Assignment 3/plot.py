import os
import pandas as pd
import matplotlib.pyplot as plt


def plot(dir: str):
    for file in os.listdir(dir):
        if file.endswith(".cwnd"):
            file_no_extension = file[:file.index(".cwnd")]
            df = pd.read_csv(os.path.join(dir, file), sep='\t',
                             header=None, names=[0, 1, 2])
            plt.plot(df[0], df[2])
            plt.title(file_no_extension)
            plt.xlabel("Time (s)")
            plt.ylabel("Congestion Window")
            plt.savefig(os.path.join(dir, file_no_extension + ".png"))
            plt.clf()


if __name__ == "__main__":
    plot("Q1/outputs")
    plot("Q2/outputs")
    plot("Q3/outputs")
