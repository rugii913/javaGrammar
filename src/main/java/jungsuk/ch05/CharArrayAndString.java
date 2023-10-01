package jungsuk.ch05;

// ch05 p.208 char[]과 String
public class CharArrayAndString {
    /*
    * TODO: char, char[], CharSequence, String에 대해서 더 생각해볼 것
    * */

    // p.210 예제 5-14
    public void charArrayAndString() {
        System.out.println("charArrayAndString()");
        String src = "ABCDE";

        for (int i = 0; i < src.length(); i++) {
            char ch = src.charAt(i); // src의 i번째 문자를 ch에 저장
            System.out.println("src.charAt(" + i + ") = " + ch);
        }

        // String을 char[]로 변환
        char[] charArray = src.toCharArray();

        // char 배열(char[])을 출력
        System.out.print("System.out.println(charArray); ===> " );
        System.out.println(charArray);

        // String 출력과 비교
        System.out.println("String 출력과 비교");
        System.out.print("System.out.println(src); ===> ");
        System.out.println(src);

        System.out.println("------------------------------------------------------------");
        // cf. 이렇게 출력할 경우 원하는 결과가 나오지 않음
        System.out.println("cf. System.out.println(\"System.out.println(charArray); ===> \" + charArray); 이렇게 출력할 경우 원하는 결과가 나오지 않음");
        System.out.println("System.out.println(charArray); ===> " + charArray);
        System.out.println("============================================================");
    }
}
