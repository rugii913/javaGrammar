package jungsuk.ch12;

public class EnumEx4 {

    public void ex4() {
        MyTransportation bus1 = MyTransportation.BUS;
        MyTransportation bus2 = MyTransportation.BUS;
        MyTransportation train = MyTransportation.TRAIN;
        MyTransportation ship = MyTransportation.SHIP;
        MyTransportation airplane = MyTransportation.AIRPLANE;

        System.out.printf("bus1 = %s, %d%n", bus1.name(), bus1.ordinal());
        System.out.printf("bus2 = %s, %d%n", bus2.name(), bus2.ordinal());
        System.out.printf("train = %s, %d%n", train.name(), train.ordinal());
        System.out.printf("ship = %s, %d%n", ship.name(), ship.ordinal());
        System.out.printf("airplane = %s, %d%n", airplane.name(), airplane.ordinal());
        System.out.println("(bus1 == bus2) = " + (bus1 == bus2));
        System.out.println("(bus1.compareTo(train)) = " + (bus1.compareTo(train)));

        System.out.println("bus1 100 distance fare is " + bus1.getFare(100));
    }

    public abstract static class MyEnum<T extends MyEnum<T>> implements Comparable<T> {

        // static int id = 0; // 예제에는 이런 방법을 사용하지만 오히려 부적절하게 보임
        private final int ordinal;
        private final String name;

        protected MyEnum(int ordinal, String name) {
            this.name = name;
            this.ordinal = ordinal;
        }

        public final int ordinal() {
            return ordinal;
        }

        public final String name() {
            return name;
        }

        @Override
        public final int compareTo(T t) {
            MyEnum<?> other = t;
            MyEnum<T> self = this;
            if (self.getClass() != other.getClass() && self.getDeclaringClass() != other.getDeclaringClass()) {
                throw new ClassCastException();
            }

            return self.ordinal - other.ordinal;
        }

        @SuppressWarnings("unchecked")
        public final Class<T> getDeclaringClass() {
            Class<?> clazz = getClass();
            Class<?> zuper = clazz.getSuperclass();
            return (zuper == MyEnum.class) ? (Class<T>) clazz : (Class<T>) zuper;
            // MyEnum 바로 아래 계층을 확인하기 위한 메서드
            // MyEnum 바로 아래라면 자신의 클래스 객체가 나올 것이고
            // MyEnum 바로 아래를 구현할 클래스라면 자신의 한 단계 위이며, MyEnum 바로 아래 클래스 객체가 나올 것임
        }

        @Override
        public String toString() {
            return name;
        }
    }

    public abstract static class MyTransportation extends MyEnum<MyTransportation> {

        public static final MyTransportation BUS = new MyTransportation("BUS", 100) {
            @Override
            int getFare(int distance) {
                return distance * getBasicFare();
            }
        };
        public static final MyTransportation TRAIN = new MyTransportation("TRAIN", 150) {
            @Override
            int getFare(int distance) {
                return distance * getBasicFare();
            }
        };
        public static final MyTransportation SHIP = new MyTransportation("SHIP", 100) {
            @Override
            int getFare(int distance) {
                return distance * getBasicFare();
            }
        };
        public static final MyTransportation AIRPLANE = new MyTransportation("AIRPLANE", 300) {
            @Override
            int getFare(int distance) {
                return distance * getBasicFare();
            }
        };

        /*
        * - 예제와 다르게 MyEnum에 ordinal 계산을 위한 값을 주지 않고, MyTransportation에 ordinal 계산을 위한 값을 부여했음
        * - 실제 Enum에서는 내부 클래스 EnumDesc를 활용하여 enum constant 생성
        *   - (찾아볼 것) EnumDesc, nominal descriptor, DynamicConstantDesc
        * */
        private static int enumSize = 0;
        private final int basicFare;

        private MyTransportation(String name, int basicFare) {
            super(enumSize++, name);
            this.basicFare = basicFare;
        }

        abstract int getFare(int distance);

        public int getBasicFare() {
            return this.basicFare;
        }
    }
}
