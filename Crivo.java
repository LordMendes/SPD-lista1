import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Crivo  {
  private ArrayList<Integer> numberList;
  private Integer pNumberPosition;

  public Crivo(int n) {
    this.pNumberPosition = -1;
    this.numberList = new ArrayList<Integer>(IntStream.range(2, n + 1)
        .boxed()
        .collect(Collectors.toList()));
  }

  private Integer getPNumber() {
    this.pNumberPosition++;
    if (this.pNumberPosition >= this.numberList.size()) {
      return null;
    }
    Integer pNumber = this.numberList.get(this.pNumberPosition);
    return pNumber;
  }

  private Boolean isMultiple(Integer pNumber, Integer testedNumber) {
    return testedNumber % pNumber == 0;
  }

  private Void removeMultiples(Integer pNumber) {
    for (int i = 0; i < numberList.size(); i++) {
      if (isMultiple(pNumber, numberList.get(i)) && numberList.get(i) != pNumber) {
        this.numberList.remove(i);
      }
    }
    return null;
  }

  public void executeAlgorithm() {
    while (this.pNumberPosition < this.numberList.size() - 1) {
      Integer pNumber = this.getPNumber();
      System.out.println(pNumber);
      this.removeMultiples(pNumber);
    }
  }

  public ArrayList<Integer> getPrimeList() {
    return this.numberList;
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

    Crivo crivo = new Crivo(arraySize);

    crivo.executeAlgorithm();

    System.out.println("Primos: " + crivo.getPrimeList());

  }

}
