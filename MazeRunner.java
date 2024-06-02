import java.util.Random;
import java.util.Scanner;

public class MazeRunner {
    static char[][] maze = {
            {'#', '!', '.', '.', 'R', '.', '.', '.', '.', '.', '.', '#', '.', '.', '.'},
            {'.', '.', '#', '.', '.', '.', '#', '.', 'H', '.', '.', '.', '.', '.', '!'},
            {'F', '.', '.', '.', '#', '.', '!', '.', '.', 'R', '.', '.', '#', '#', '.'},
            {'.', '.', '#', '.', '.', '#', '.', '.', '.', '.', 'F', '.', '.', '.', '.'},
            {'.', '!', '.', '.', '#', '.', '#', '.', '#', '.', '.', '#', '.', '.', '.'},
            {'.', '.', 'H', '.', '.', '!', '.', '.', 'H', '.', '.', 'F', '.', '.', 'R'},
            {'#', '#', '#', '#', '.', '.', '#', '.', '.', '.', 'T', '.', '.', '.', 'E'},
            {'.', '.', '#', '.', 'F', '.', '#', '#', '.', '#', '#', '#', '#', '.', '.'},
            {'.', '#', '.', '.', '.', '.', '!', '.', '#', '.', '.', '.', '#', '.', '.'},
            {'.', 'T', 'T', '.', '#', '#', '.', '.', '.', '.', 'T', '.', '.', '.', 'R'},
            {'.', '.', '.', '#', '.', '.', '.', '#', '.', '#', '.', '#', '.', 'T', '.'},
            {'B', '.', '#', '.', '.', '!', '.', '!', '.', '.', '.', '.', '.', '.', '#'},
            {'.', '.', '.', 'F', '!', '.', '.', '.', 'H', '.', '.', 'R', '.', '.', '.'},
            {'.', '.', 'H', '.', '.', '.', '!', '.', '.', '.', '#', '.', '.', '#', '.'},
            {'.', '.', '.', '#', '.', '.', '#', '.', '#', '.', '#', '.', '.', '#', '#'}};
    static int playerX, playerY;
    static int hasR, hasF, hasT, hasH; // Bonusların durumunu tutar.
    static int moveCount = 0;

    public static void main(String[] args) {
        int rearrangeCounter = 1; // Bonus ve mayınların yerini güncellemek için 5'in katlarını tutan değişken
        initializeGame(); // Labirenti ve oyuncu konumunu başlat
        Scanner scanner = new Scanner(System.in);

        while (true) {
            printMaze(); // Labirentin güncel durumunu yazdır
            
            System.out.println("Adım Sayısı: " + moveCount);
            System.out.println("Bulunduğunuz konum: (" + playerX + ", " + playerY + ")");
            System.out.println("W, A, S, D karakterlerinden birini giriniz ya da bonus kullanmak için + karakterine basınız. Çıkış için “exit” yazınız.");

            String input = scanner.nextLine();
            if (input.equals("exit")) {
                break;
            }

            // Oyuncu girdisine göre hareketi işle
            processInput(input);

            // Oyun bitiş koşulunu kontrol et
            if (checkWinCondition()) {
                System.out.println("Tebrikler! Oyunu kazandınız.");
                break;
            }

            // Her 5 adımda bonus ve mayınların yerlerini güncelle
            if (moveCount >= rearrangeCounter * 5) {
                rearrangeBonuses(maze);
                rearrangeMines(maze);
                rearrangeCounter++;
            }
        }

        scanner.close();
    }

    public static void initializeGame() {
        // Başlangıç ve bitiş noktalarını bulma
        findStartAndEnd();

        // Bonus miktarlarının başlangıç durumunu ayarlama
        int hasR = 0;
        int hasF = 0;
        int hasT = 0;
        int hasH = 0;

        // Adım sayısını sıfırlama
        moveCount = 0;
    }

    public static boolean checkWinCondition() {
        // Bitiş noktası 'E' harfi ile temsil edilir
        return maze[playerX][playerY] == 'E';
    }

    public static void findStartAndEnd() {
        // Başlangıç noktasını (B) bulma
        for (int i = 0; i < maze.length; i++) {
            if (maze[i][0] == 'B') {
                playerX = i;
                playerY = 0;
                break;
            }
        }

        // Bitiş noktasını (E) bulma - Burada bitiş noktası sadece bilgi amaçlı bulunuyor,
        // bu bilgi oyunun diğer kısımlarında kullanılabilir.
        for (char[] chars : maze) {
            if (chars[maze[0].length - 1] == 'E') {
                // Bitiş noktasının x ve y koordinatları (i, maze[0].length - 1)
                break;
            }
        }
    }

    public static void processInput(String input) {
        switch (input.toUpperCase()) {
            case "W":
                movePlayer(-1, 0);
                break;
            case "A":
                movePlayer(0, -1);
                break;
            case "S":
                movePlayer(1, 0);
                break;
            case "D":
                movePlayer(0, 1);
                break;
            case "+":
                useBonus();
                break;
            default:
                System.out.println("Geçersiz giriş. Lütfen 'W', 'A', 'S', 'D' veya '+' giriniz.");
                break;
        }
    }

    public static void movePlayer(int dx, int dy) {
        int newX = playerX + dx;
        int newY = playerY + dy;

        if (isValidMove(newX, newY)) {
            playerX = newX;
            playerY = newY;
            System.out.println("Yeni konumunuz: (" + playerX + ", " + playerY + ")");
            checkForBonus();
            moveCount++; // Geçerli harekette sayacı artır
        } else {
            System.out.println("Geçersiz hareket veya engelle karşılaşıldı.");
        }
    }

    public static boolean isValidMove(int x, int y) {
        // Labirent sınırları içinde olup olmadığını kontrol et
        if (x < 0 || x >= maze.length || y < 0 || y >= maze[0].length) {
            moveCount++;
            return false;
        }

        // Engelleri kontrol et
        char cell = maze[x][y];
        switch (cell) {
            case '#': // Duvar
                if (hasR > 0) {
                    // 'R' bonusu varsa duvarı yık
                    maze[x][y] = '.';
                    hasR--;
                    return true;
                } else {
                    // Duvara çarptı, hamle sayısı artar
                    moveCount++;
                    return false;
                }
            case '!': // Mayın
                if (hasF > 0) {
                    // 'F' bonusu varsa mayını çöz
                    maze[x][y] = '.';
                    hasF--;
                    return true;
                } else {
                    // Mayına çarptı, hamle sayısı artar
                    moveCount += 5;
                    return false;
                }
            default:
                return true; // Boş alan veya bonus
        }
    }

    public static void checkForBonus() {
        // Oyuncunun bulunduğu konumdaki hücreyi kontrol et
        char cell = maze[playerX][playerY];
        switch (cell) {
            case 'T':
                hasT++;
                break;
            case 'R':
                hasR++;
                break;
            case 'H':
                hasH++;
                break;
            case 'F':
                hasF++;
                break;
        }

        if (cell == 'T' || cell == 'R' || cell == 'H' || cell == 'F') {
            System.out.println(cell + " bonusu toplandı.");
            maze[playerX][playerY] = '.'; // Bonus toplandıktan sonra boş hücreye dönüştür
        }
    }

    public static void useBonus() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Kullanmak istediğin bonusu seç (T, H, R, F): ");
        String bonusType = scanner.nextLine();

        switch (bonusType.toUpperCase()) {
            case "T":
                if (hasT > 0) {
                    useTeleportBonus(scanner);
                } else {
                    System.out.println("T bonusuna sahip değilsiniz.");
                }
                break;
            case "R":
                if (hasR > 0) {
                    useWallRemovalBonus();
                } else {
                    System.out.println("R bonusuna sahip değilsiniz.");
                }
                break;
            case "H":
                if (hasH > 0) {
                    useMoveReductionBonus();
                } else {
                    System.out.println("H bonusuna sahip değilsiniz.");
                }
                break;
            case "F":
                if (hasF > 0) {
                    useMineDisarmBonus();
                } else {
                    System.out.println("F bonusuna sahip değilsiniz.");
                }
                break;
            default:
                System.out.println("Geçersiz bonus. Lütfen geçerli bir bonus seçin.");
                break;
        }
    }

    public static void useTeleportBonus(Scanner scanner) {
        int x, y;
        do {
            System.out.println("Işınlanmak istediğiniz dikey ve yatay koordinatları giriniz (örn: 3 4): ");
            x = scanner.nextInt();
            y = scanner.nextInt();
        } while (x < 0 || x >= maze.length || y < 0 || y >= maze[0].length || maze[x][y] == '#' || maze[x][y] == '!');

        playerX = x;
        playerY = y;
        hasT--;
        System.out.println("Işınlandınız: Yeni konumunuz (" + playerX + ", " + playerY + ")");
    }

    public static void useWallRemovalBonus() {
        // Duvar kaldırma bonusu; bu bonus sadece duvar ile karşılaşıldığında çağrılmalıdır.
        if (maze[playerX][playerY] == '#') {
            maze[playerX][playerY] = '.';
            hasR--;
            System.out.println("'R' bonusu kullanıldı ve duvar kaldırıldı.");
        } else {
            System.out.println("'R' bonusu sadece duvar ile karşılaşıldığında kullanılabilir.");
        }
    }

    public static void useMoveReductionBonus() {
        moveCount = Math.max(moveCount - 2, 0);
        hasH--;
        System.out.println("'H' bonusu kullanıldı ve hamle sayısı azaltıldı. Yeni hamle sayısı: " + moveCount);
    }

    public static void useMineDisarmBonus() {
        // Mayın çözme bonusu; bu bonus sadece mayın ile karşılaşıldığında çağrılmalıdır.
        if (maze[playerX][playerY] == '!') {
            maze[playerX][playerY] = '.';
            hasF--;
            System.out.println("'F' bonusu kullanıldı ve mayın çözüldü.");
        } else {
            System.out.println("'F' bonusu sadece mayın ile karşılaşıldığında kullanılabilir.");
        }
    }

    public static void rearrangeBonuses(char[][] maze) {
        char[] bonuses = {'T', 'R', 'H', 'F'};
        char[] bonusRestrictions = {'#', '!', 'B', 'E'};
        Random random = new Random();

        // Mevcut bonusların pozisyonlarını ve türlerini depola
        char[][] bonusPositions = new char[maze.length * maze[0].length][3]; // 3. eleman bonus türünü tutacak
        int bonusCount = 0;

        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[i].length; j++) {
                if (contains(bonuses, maze[i][j])) {
                    bonusPositions[bonusCount][0] = (char) i; // X koordinatı
                    bonusPositions[bonusCount][1] = (char) j; // Y koordinatı
                    bonusPositions[bonusCount][2] = maze[i][j]; // Bonus türü
                    bonusCount++;
                    maze[i][j] = '.'; // Bonusun eski pozisyonunu boşalt
                }
            }
        }

        // Bonusları yeni rastgele konumlara yerleştir
        int[][] availablePositions = findAvailablePositions(maze, bonusRestrictions);
        for (int i = 0; i < bonusCount; i++) {
            if (availablePositions.length == 0) {
                break;
            }
            int randomPositionIndex = random.nextInt(availablePositions.length);
            int[] newPosition = availablePositions[randomPositionIndex];
            maze[newPosition[0]][newPosition[1]] = bonusPositions[i][2]; // Bonus türünü yeni pozisyona yerleştir
            availablePositions = removeElementFromArray(availablePositions, randomPositionIndex);
        }
    }


    private static int[][] removeElementFromArray(int[][] array, int index) {
        if (array == null || index < 0 || index >= array.length) {
            return array;
        }

        int[][] newArray = new int[array.length - 1][2];
        for (int i = 0, j = 0; i < array.length; i++) {
            if (i != index) {
                newArray[j++] = array[i];
            }
        }
        return newArray;
    }

    public static void rearrangeMines(char[][] maze) {
        char mine = '!';
        char[] mineRestrictions = {'T', 'R', 'H', 'F', '#', 'B', 'E'};
        Random random = new Random();

        // Mevcut mayınların listesini oluştur ve labirentten kaldır
        int[][] minePositions = new int[maze.length * maze[0].length][2];
        int mineCount = 0;
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[i].length; j++) {
                if (maze[i][j] == mine) {
                    minePositions[mineCount][0] = i;
                    minePositions[mineCount][1] = j;
                    mineCount++;
                    maze[i][j] = '.'; // Mayının eski pozisyonunu boşalt
                }
            }
        }

        // Mayınları yeni rastgele konumlara yerleştir
        int[][] availablePositions = findAvailablePositions(maze, mineRestrictions);
        for (int i = 0; i < mineCount; i++) {
            if (availablePositions.length == 0) {
                break; // Eğer uygun konum kalmadıysa döngüden çık
            }
            int randomPositionIndex = random.nextInt(availablePositions.length);
            int[] newPosition = availablePositions[randomPositionIndex];
            maze[newPosition[0]][newPosition[1]] = mine;
            availablePositions = removeElementFromArray(availablePositions, randomPositionIndex);
        }
    }

    public static int[][] findAvailablePositions(char[][] maze, char[] restrictions) {
        int[][] positions = new int[maze.length * maze[0].length][2];
        int count = 0;

        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[i].length; j++) {
                if (!contains(restrictions, maze[i][j])) {
                    positions[count][0] = i;
                    positions[count][1] = j;
                    count++;
                }
            }
        }

        return trimArray(positions, count);
    }

    private static int[][] trimArray(int[][] array, int size) {
        int[][] trimmedArray = new int[size][2];
        for (int i = 0; i < size; i++) {
            System.arraycopy(array[i], 0, trimmedArray[i], 0, array[i].length);
        }
        return trimmedArray;
    }

    public static boolean contains(char[] array, char value) {
        for (char c : array) {
            if (c == value) {
                return true;
            }
        }
        return false;
    }

    public static void printMaze() {
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[i].length; j++) {
                if (i == playerX && j == playerY) {
                    System.out.print("X "); // Oyuncunun konumunu 'X' ile işaretle
                } else {
                    System.out.print(maze[i][j] + " "); // Labirentin diğer hücrelerini yazdır
                }
            }
            System.out.println(); // Her satırdan sonra bir satır boşluk bırak
        }
    }

}