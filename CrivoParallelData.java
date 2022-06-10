import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CrivoParallelData extends Thread {
  private ArrayList<Integer> numberList;
  private Integer pNumber;
  private Integer index;

  public CrivoParallelData(ArrayList<Integer> numberList, Integer pNumber, Integer index) {
    this.pNumber = pNumber;
    this.numberList = numberList;
    this.index = index;
  }

  private Boolean isMultiple(Integer pNumber, Integer testedNumber) {
    return testedNumber % pNumber == 0;
  }

  private void removeMultiple() {
    if (isMultiple(this.pNumber, numberList.get(this.index)) && numberList.get(this.index) != this.pNumber) {
      this.numberList.set(this.index, 0);
    }
  }

  public void run() {
    this.removeMultiple();
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
    Integer index = 0;
    long startTime = System.currentTimeMillis();

      while (index < numberList.size()) {
        ArrayList<CrivoParallelData> threads = new ArrayList<CrivoParallelData>();
        for (int i = 0; i < nThreads; i++) {
          threads.add(new CrivoParallelData(numberList, pNumber, index));

          index++;
          if (index > numberList.size() - 1) {
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
      }

    long endTime = System.currentTimeMillis();
    System.out.println("Tempo de execução: " + (endTime - startTime) + "ms");
    numberList.removeIf(filter -> filter == 0);
    // System.out.println("Numeros primos: " + numberList);

  }

}
