import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CrivoParallelControl extends Thread {
  private ArrayList<Integer> numberList;
  private Integer pNumber;

  public CrivoParallelControl(ArrayList<Integer> numberList, Integer pNumber) {
    this.pNumber = pNumber;
    this.numberList = numberList;
  }

  private Boolean isMultiple(Integer pNumber, Integer testedNumber) {
    return testedNumber % pNumber == 0;
  }

  private void removeMultiples(Integer pNumber) {
    for (Integer i = pNumber; i < numberList.size(); i++) {
      if (isMultiple(pNumber, numberList.get(i)) && numberList.get(i) != pNumber) {
        this.numberList.set(i, 0); // apagar dá problema do escritor e leitor
      }
    }
  }

  public void run() {
    this.removeMultiples(this.pNumber);
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
    Integer k = 1;

    long startTime = System.currentTimeMillis();
    Boolean minusPlus = false;
    while (Math.pow(pNumber, 2) < arraySize) {
      ArrayList<CrivoParallelControl> threads = new ArrayList<CrivoParallelControl>();
      for (int i = 0; i < nThreads; i++) {
        threads.add(new CrivoParallelControl(numberList, pNumber));
        if (pNumber > 3) {
          pNumber = minusPlus ? 6 * k + 1 : 6 * k - 1;
          minusPlus = !minusPlus;
          if(minusPlus) k++;
        } else {
          pNumber++;
        }
      }
      for (CrivoParallelControl thread : threads) {
        thread.start();
      }
      for (CrivoParallelControl thread : threads) {
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
    System.out.println("Numeros primos: " + numberList);
    System.out.println("Quantidade de numeros primos: " + numberList.size());

  }

}
