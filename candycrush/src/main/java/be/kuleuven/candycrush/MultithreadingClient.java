package be.kuleuven.candycrush;

public class MultithreadingClient {
    public static void main(String[] args) {
        int rijen = 10;
        int kolomen = 10;

        BoardSize size = new BoardSize(rijen,kolomen);
        Board<Integer> board = new Board<>(size);

        Thread thread1 = new Thread(() -> {
            threadingLogic(size, board, 1);
        });

        Thread thread2 = new Thread(() -> {
            threadingLogic(size, board, 2);
        });

        thread1.start();
        thread2.start();
    }

    private static void threadingLogic(BoardSize size, Board<Integer> board, int index) {
        while (true) {
            int x = (int) (Math.random() * 10);
            int y = (int) (Math.random() * 10);
            Position position = new Position(x, y, size);

            board.replaceCellAt(position, index);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("swap :)" + index);
        }
    }
}
