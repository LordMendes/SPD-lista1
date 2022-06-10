import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CrivoParallelData extends Thread {
  private ArrayList<Integer> numberList;
  private Integer pNumber;
  private Integer start;
  private Integer end;

  public CrivoParallelData(ArrayList<Integer> numberList, Integer pNumber, Integer start, Integer end) {
    this.pNumber = pNumber;
    this.numberList = numberList;
    this.start = start;
    this.end = end;
  }

  private Boolean isMultiple(Integer pNumber, Integer testedNumber) {
    return testedNumber % pNumber == 0;
  }

  private void removeMultiples() {
    for (Integer i = start; i <= end; i++) {
      if (isMultiple(pNumber, numberList.get(i)) && numberList.get(i) != pNumber) {
        this.numberList.set(i, 0);
      }
    }
  }

  public void run() {
    this.removeMultiples();
  }

  public static void main(String[] args) {
    Integer arraySize;
    Integer nThreads;

    if (args.length > 1) {
      arraySize = Integer.parseInt(args[0]);
      nThreads = Integer.parseInt(args[1]);
    } else {
      Scanner input = new Scanner(System.in);
      System.out.println("Digite o tamanho da lista:");
      arraySize = input.nextInt();
      System.out.println("Digite a quantidade de threads:");
      nThreads = input.nextInt();
      input.close();

      System.out.print("\033[H\033[2J");
      System.out.flush();
    }
    ArrayList<Integer> numberList = new ArrayList<Integer>(IntStream.range(2, arraySize + 1)
        .boxed()
        .collect(Collectors.toList()));

    Integer pNumber = 2;

    int pieces = numberList.size() / nThreads;
    int rest = numberList.size() % nThreads;
    int pieceSize = pieces * nThreads;
    int start = 0;
    int end = pieceSize - 1;
    Boolean minusPlus = false;
    Integer k = 1;
    long startTime = System.currentTimeMillis();

    while (Math.pow(pNumber,2) < arraySize) {
      ArrayList<CrivoParallelData> threads = new ArrayList<CrivoParallelData>();
      for (int i = 0; i < nThreads; i++) {
        if (end < numberList.size() - rest - 1) {
          end = start + pieceSize - 1;
          threads.add(new CrivoParallelData(numberList, pNumber, start, end));
          start += pieceSize;
        } else {
          end = numberList.size() - 1;
          threads.add(new CrivoParallelData(numberList, pNumber, start, end));
          start += rest;
        }
        if (start >= numberList.size()) {
          break;
        }
      }
      for (CrivoParallelData thread : threads) {
        thread.start();
      }
      for (CrivoParallelData thread : threads) {
        try {
          thread.join();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
      if (pNumber > 3) {
        pNumber = minusPlus ? 6 * k + 1 : 6 * k - 1;
        minusPlus = !minusPlus;
        if (minusPlus)
          k++;
      } else {
        pNumber++;
      }
      start=0;
    }

    long endTime = System.currentTimeMillis();
    System.out.println("Tempo de execução: " + (endTime - startTime) + "ms");
    numberList.removeIf(filter -> filter == 0);
    System.out.println("Numeros primos: " + numberList);
    System.out.println("Quantidade de numeros primos: " + numberList.size());

  }

}
