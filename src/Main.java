import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    static boolean onlyFrom0to10=true;  //нстройка ограничения вводимого числа только от 0 до 10(по усл. задачи)
    static boolean only2digit=true;  //нстройка ограничения кол-ва вводимых чисел только 2-мя (по усл. задачи)

    enum RomArab { Arab(),Rom(),NoElse(),Err();

        RomArab() {
        }
    }
    public static RomArab tipRA;

    enum Action{
        Plus,Minus,Div,Mult,Eqv,Err
    }
    public static Action action=null;

    public static void main(String[] args) throws IOException {

        System.out.println("Введи выражение вида 1+9 или римск. вида VI / III.");

        Scanner sc=new Scanner(System.in);
        try {
            Pattern pattern=Pattern.compile("(?:([0-9]+)|([IVXLDMC]+))(\\s*[*/+-]\\s*)?+");
            while (sc.hasNext()) {
                int cntDig=0;
                String inputs=sc.nextLine();
                Matcher matcher=pattern.matcher(inputs);
                Expression exp=new Expression();
                tipRA=RomArab.NoElse;
                while (matcher.find()) {
                    int ival=-1;
                    if (matcher.start(1)>=0) {  //обнаружена гр. арабских
                        String si=matcher.group(1);
                        if (tipRA ==RomArab.Rom) {
                            throw new NoValidateTipExeption("НЕ РИМСКАЯ ЦИФРА:"+si);
                        }
                        tipRA=RomArab.Arab;
                        cntDig++;
                        ival=StringParser.toInt(si);
                        if (onlyFrom0to10 && (ival< 0 || ival>10)) throw new NoValidateTipExeption("Только от 0 до 10ти или присвойте onlyFrom0to10=false , а ввели:"+si);
                        if (only2digit && (cntDig>2)) throw new NoValidateTipExeption("Только 2 числа или присвойте only2digit=false , а ввели:"+cntDig);

                    }
                    if (matcher.start(2)>=0) { //обнаружена гр. римских цифр
                        String si=matcher.group(2);
                        if (tipRA ==RomArab.Arab) {
                            throw new NoValidateTipExeption("НЕ АРАБСКАЯ ЦИФРА:"+si);
                        }
                        tipRA=RomArab.Rom;
                        ival=StringParser.toInt(si);
                        if (onlyFrom0to10 && (ival< 0 || ival>10)) throw new NoValidateTipExeption("Только от 0 до 10ти, или присвойте onlyFrom0to10=false, а ввели:"+si);
                        if (only2digit && (cntDig>2)) throw new NoValidateTipExeption("Только 2 числа или присвойте only2digit=false , а ввели:"+cntDig);

                    }
                    if (matcher.start(3)>=0) {
                        String sz = matcher.group(3).trim();  //знак
                        action = StringParser.toAction(sz);
                    } else action=Action.Eqv;
                    exp.calc(ival,action);

                }
                System.out.println("Результат="+exp.getiRez());

            }
        } catch (Exception | NoValidateTipExeption e) {
            System.out.println("Что-то пошло не так:"+e.getMessage());
        }
        sc.close();

    }
}
