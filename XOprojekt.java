/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xoprojekt;

import java.io.File;         //
import java.io.FileWriter;   //Pro ulozeni vysledku hry
import java.io.IOException;  //
import java.util.Random;     //Pro rezim jednoho hrace
import java.util.Scanner;    //Pro zadavani hodnot
import java.util.InputMismatchException;//Pripad, kdyby nekdo chtel misto cisel zadat text

/**
 *
 * @author Kartavykh Aleksandr, 3x3 Piskvorky s nekolika rezimy hry a ulozenim vysledku hry
 */
public class XOprojekt {

    public static final String USER_SIGN = "X";//Znak prvniho hrace
    public static final String USER_SIGN_SECOND = "O";//Znak druheho hrace
    public static final String AI_SIGN = "O";//nebo pocitace
    public static final String NOT_SIGN = "*";//Znak oznacujici prazdne pole
    public static int aiLevel = 0;//Promenna pro uroven obtiznosti v rezimu jednoho hrace
    public static final int DIMENSION = 3;//Rozmer herniho pole
    public static String[][] field = new String[DIMENSION][DIMENSION];//Samotne pole

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        mainMenu();//Volani hlavniho menu a zacatek hry
    }

    public static void mainMenu() throws IOException {//metoda pro spousteni hry
        Scanner klv = new Scanner(System.in);
        int i;//promenna pro cykly switch
        try {//opatreni proti spatnym vstupnim datam
            do {//cyklus pro pripad spatne volby
                System.out.println("Zvolte rezim hry");
                System.out.println("1) Jeden hrac ");
                System.out.println("2) Dva hrace ");
                System.out.println("3) Ukonceni hry ");
                i = klv.nextInt();
                switch (i) {//vetveni pro volbu rezimu hry
                    case 1: {//rezim jednoho hrace
                        do {//cyklus pro pripad spatne volby
                            System.out.println("Zvolte obtiznost hry");
                            System.out.println("1)Jednoducha");
                            System.out.println("2)Normalni");
                            System.out.println("3)Tezka");
                            i = klv.nextInt();
                            switch (i) {//vetveni pro volbu obtiznosti
                                case 1:
                                    aiLevel = 0;
                                    modeAgainstAI();
                                    break;
                                case 2:
                                    aiLevel = 1;
                                    modeAgainstAI();
                                    break;
                                case 3:
                                    aiLevel = 2;
                                    modeAgainstHardAI();
                                    break;
                                default:
                                    System.out.println("Takova volba neexistuje!");
                                    break;
                            }
                        } while (i < 1 || i > 3);
                        break;
                    }
                    case 2: {//rezim pro dva hrace
                        modeTwoPlayers();
                        break;
                    }
                    case 3: {//ukonceni hry
                        System.exit(0);
                        break;
                    }
                    default: {
                        System.out.println("Takova volba neexistuje!");
                        break;
                    }
                }
            } while (i < 1 || i > 3);
        } catch (InputMismatchException ex) {//opatreni proti spatnym vstupnim datam
            System.err.println("Musite zadavat cisla! Restartujte hru a budte opatrni!");
        }
    }

    public static void modeTwoPlayers() throws IOException {//metoda pro hru ve dvou
        int count = 0;//promenna pro pocet kol
        initField();//inicializace pole
        printField();//vypis pole na obrazovku
        while (true) {//cyklus pro hru
            System.out.println("1. hrac");
            userShot(USER_SIGN);//kolo 1. hrace
            count++;
            printField();//vypis pole na obrazovku
            if (checkWin(USER_SIGN)) {//kontrola vitezstvi
                int WINNER = 1;//promenna pro vetveni pro ulozeni hry (viz dole)
                WriteFile(WINNER);//ulozeni vysledku
                System.out.println("Vyhral 1. hrac!!!");
                break;
            }
            if (count == Math.pow(DIMENSION, 2)) {//konec hry v pripade remizy
                int WINNER = 0;//promenna pro vetveni pro ulozeni hry (viz dole)
                WriteFile(WINNER);//ulozeni vysledku
                System.out.println("Remiza!!!");
                System.exit(0);
                break;
            }
            System.out.println("2. hrac");
            userShot(USER_SIGN_SECOND);//kolo 2. hrace
            count++;
            printField();//vypis pole na obrazovku
            if (checkWin(USER_SIGN_SECOND)) {//kontrola vitezstvi
                int WINNER = 2;//promenna pro vetveni pro ulozeni hry (viz dole)
                WriteFile(WINNER);//ulozeni vysledku
                System.out.println("Vyhral 2. hrac!!!");
                break;
            }
        }
    }

    public static void modeAgainstAI() throws IOException {//metoda pro hru proti pocitaci
        int count = 0;//promenna pro pocet kol
        initField();//inicializace pole
        while (true) {//cyklus pro hru
            printField();//vypis pole na obrazovku
            userShot(USER_SIGN);//kolo hrace
            count++;
            if (checkWin(USER_SIGN)) {//kontrola vitezstvi
                printField();//vypis pole na obrazovku
                int WINNER = 4;//promenna pro vetveni pro ulozeni hry (viz dole)
                WriteFile(WINNER);//ulozeni vysledku
                System.out.println("Vyhral uzivatel!!!");
                break;
            }
            if (count == Math.pow(DIMENSION, 2)) {//konec hry v pripade remizy
                printField();//vypis pole na obrazovku
                int WINNER = 0;//promenna pro vetveni pro ulozeni hry (viz dole)
                WriteFile(WINNER);//ulozeni vysledku
                System.out.println("Remiza!!!");
                System.exit(0);
                break;
            }
            aiShot();//kolo pocitace
            count++;
            System.out.println(" ");
            if (checkWin(AI_SIGN)) {//kontrola vitezstvi
                printField();//vypis pole na obrazovku
                int WINNER = 3;//promenna pro vetveni pro ulozeni hry (viz dole)
                WriteFile(WINNER);//ulozeni vysledku
                System.out.println("Vyhral pocitac!!!");
                break;
            }
        }
    }

    public static void modeAgainstHardAI() throws IOException {//metoda pro hru proti chytremu pocitaci
        int count = 0;//promenna pro pocet kol
        initField();//inicializace pole
        while (true) {//cyklus pro hru
            aiShot();//kolo pocitace
            count++;
            System.out.println(" ");
            printField();//vypis pole na obrazovku
            if (checkWin(AI_SIGN)) {//kontrola vitezstvi
                int WINNER = 3;//promenna pro vetveni pro ulozeni hry (viz dole)
                WriteFile(WINNER);//ulozeni vysledku
                System.out.println("Vyhral pocitac!!!");
                break;
            }
            if (count == Math.pow(DIMENSION, 2)) {//konec hry v pripade remizy
                int WINNER = 0;//promenna pro vetveni pro ulozeni hry (viz dole)
                WriteFile(WINNER);//ulozeni vysledku
                System.out.println("Remiza!!!");
                System.exit(0);
                break;
            }
            userShot(USER_SIGN);//kolo hrace
            count++;
            if (checkWin(USER_SIGN)) {//kontrola vitezstvi
                printField();//vypis pole na obrazovku
                int WINNER = 4;//promenna pro vetveni pro ulozeni hry (viz dole)
                WriteFile(WINNER);//ulozeni vysledku
                System.out.println("Vyhral uzivatel!!!");
                break;
            }
        }
    }

    public static void initField() {//metoda pro naplneni pole "prazdnymi" znaky
        for (int i = 0; i < DIMENSION; i++) {
            for (int j = 0; j < DIMENSION; j++) {
                field[i][j] = NOT_SIGN;
            }
        }
    }

    public static void printField() {//metoda pro vypis pole
        for (int i = 0; i <= DIMENSION; i++) {
            System.out.print(i + "_|_");
        }
        System.out.println();
        for (int i = 0; i < DIMENSION; i++) {
            System.out.print((i + 1) + "_|_");
            for (int j = 0; j < DIMENSION; j++) {
                System.out.print(field[i][j] + "_|_");
            }
            System.out.println("");
        }
    }

    public static void userShot(String sign) {//metoda pro kolo hrace
        int x = -1;//promenne pro souradnice 
        int y = -1;
        do {//cyklus pro volbu souradnic
            Scanner klv = new Scanner(System.in);
            do {//cyklus pro volbu souradnice x
                System.out.println("Zadejte souradnici x (1 az "+ DIMENSION + "): ");
            x = klv.nextInt() - 1;
            if (x < 0 || x > DIMENSION - 1) {
                System.out.println("Zadal jste spatnou hodnotu");
            }
            } while (x < 0 || x > DIMENSION - 1);
            do {//cyklus pro volbu souradnice y
                System.out.println("Zadejte souradnici y (1 az "+ DIMENSION + "): ");
            y = klv.nextInt() - 1;
            if (y < 0 || y > DIMENSION - 1) {
                System.out.println("Zadal jste spatnou hodnotu");
            }
            } while (y < 0 || y > DIMENSION - 1);
            if (isCellBusy(x,y)==true) {
                System.out.println("Pole je jiz obsazeno!");
            }
        } while (isCellBusy(x, y));
        field[x][y] = sign;
    }

    public static void aiShot() {//metoda pro kolo pocitace
        int x = -1;//promenne pro souradnice
        int y = -1;
        boolean ai_win = false;//promenne pro zlepseni schopnosti pocitace
        boolean user_win = false;
        // aiLevel = 2
        // Hledani vitezneho kroku
        if (aiLevel == 2) {
            for (int i = 0; i < DIMENSION; i++) {
                for (int j = 0; j < DIMENSION; j++) {
                    if (!isCellBusy(i, j)) {
                        field[i][j] = AI_SIGN;
                        if (checkWin(AI_SIGN)) {
                            x = i;//ulozeni nalezenych souradnic pro krok
                            y = j;
                            ai_win = true;
                        }
                        field[i][j] = NOT_SIGN;
                    }
                }

            }
        }
        // aiLevel = 1
        // Zabraneni uzivateli ve vitezstvi
        if (aiLevel > 0) {
            if (!ai_win) {
                for (int i = 0; i < DIMENSION; i++) {
                    for (int j = 0; j < DIMENSION; j++) {
                        if (!isCellBusy(i, j)) {
                            field[i][j] = USER_SIGN;
                            if (checkWin(USER_SIGN)) {
                                x = i;//ulozeni nalezenych souradnic pro krok
                                y = j;
                                user_win = true;
                            }
                            field[i][j] = NOT_SIGN;
                        }
                    }
                }
            }
        }
        //aiLevel = 0
        if (!ai_win && !user_win) {//nahodny krok pro nejnizsi uroven intelektu pocitace
            do {
                Random rnd = new Random();
                x = rnd.nextInt(DIMENSION);
                y = rnd.nextInt(DIMENSION);
            } while (isCellBusy(x, y));
        }
        field[x][y] = AI_SIGN;
    }

    public static boolean isCellBusy(int x, int y) {//kontrola obsazenosti policka
        return !field[x][y].equals(NOT_SIGN);
    }

    public static boolean checkLine(int start_x, int start_y, int dx, int dy, String sign) {
        //metoda pro kontrolu radku/sloupcu/diagonal
        for (int i = 0; i < DIMENSION; i++) {
            if (!field[start_x + i * dx][start_y + i * dy].equals(sign)) {
                return false;
            }
        }
        return true;
    }

    public static boolean checkWin(String sign) {//metoda pro kontrolu vitezstvi pro vsechny varianty
        for (int i = 0; i < DIMENSION; i++) {
            // kontrola radku
            if (checkLine(i, 0, 0, 1, sign)) {
                return true;
            }
            // kontrola sloupcu
            if (checkLine(0, i, 1, 0, sign)) {
                return true;
            }
        }
        // kontrola diagonal
        if (checkLine(0, 0, 1, 1, sign)) {
            return true;
        }
        if (checkLine(0, DIMENSION - 1, 1, -1, sign)) {
            return true;
        }
        return false;
    }

    public static void WriteFile(int WINNER) throws IOException {//metoda pro ulozeni vysledku hry
        FileWriter writer = new FileWriter(new File("C:\\Users\\12\\Desktop\\Vysledky.txt"), true);
        StringBuilder result = new StringBuilder();//deklarace promenne StringBuilder pro nacteni do ni herniho pole
        String separator=System.getProperty("\r\n");//Mam Windows, proto jsem mel deklarovat odradkovani
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                result.append("_").append(field [i][j]).append("_|");//naplneni promenne 
            }
            result.append("\r\n");
        }
        switch (WINNER) {//vetveni pro ruzne rezimy a vysledky
            case 0:
                result.append("Remiza!");
                result.append("\r\n");
                break;
            case 1:
                result.append("Vyhral 1. hrac!");
                result.append("\r\n");
                break;
            case 2:
                result.append("Vyhral 2. hrac!");
                result.append("\r\n");
                break;
            case 3:
                result.append("Vyhral pocitac!");
                result.append("\r\n");
                break;
            case 4:
                result.append("Vyhral uzivatel!");
                result.append("\r\n");
                break;
        }
        writer.write(result.toString());//zapis vysledku do .txt souboru
        writer.flush();
    }
    
}
