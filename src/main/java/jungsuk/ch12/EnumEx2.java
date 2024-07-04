package jungsuk.ch12;

public class EnumEx2 {

    public void ex2() {
        for (Direction direction : Direction.DIRECTIONS) {
            System.out.printf("%s = %d%n", direction.name(), direction.getValue());
        }

        Direction east = Direction.EAST;
        Direction west = Direction.WEST;

        System.out.println("east = " + east);
        System.out.println("west = " + west);

        System.out.println("시계방향 0, 1, 2, 3, 4, 5 회전");
        System.out.println(Direction.EAST.rotateClockwise(0));
        System.out.println(Direction.EAST.rotateClockwise(1));
        System.out.println(Direction.EAST.rotateClockwise(2));
        System.out.println(Direction.EAST.rotateClockwise(3));
        System.out.println(Direction.EAST.rotateClockwise(4));
        System.out.println(Direction.EAST.rotateClockwise(5));
        System.out.println("반시계방향 0, 1, 2, 3, 4, 5 회전");
        System.out.println(Direction.EAST.rotateClockwise(0));
        System.out.println(Direction.EAST.rotateClockwise(-1));
        System.out.println(Direction.EAST.rotateClockwise(-2));
        System.out.println(Direction.EAST.rotateClockwise(-3));
        System.out.println(Direction.EAST.rotateClockwise(-4));
        System.out.println(Direction.EAST.rotateClockwise(-5));
    }


    enum Direction {
        EAST(1, "→"), SOUTH(2, "↓"), WEST(3, "←"), NORTH(4, "↑");

        private static final Direction[] DIRECTIONS = Direction.values();
        private final int value;
        private final String symbol;

        Direction(int value, String symbol) {
            this.value = value;
            this.symbol = symbol;
        }

        public int getValue() {
            return value;
        }

        public String getSymbol() {
            return symbol;
        }

        public static Direction of(int directionValue) {
            if (directionValue < 1 || directionValue > 4) {
                throw new IllegalArgumentException("Invalid value: " + directionValue);
            }
            return DIRECTIONS[directionValue - 1];
        }

        public Direction rotateClockwise(int numberOfRotations) {
            if (numberOfRotations < 0) {
                numberOfRotations = 4 - ((numberOfRotations * -1) % 4);
            }

            return DIRECTIONS[(value + numberOfRotations - 1) % 4];
        }

        @Override
        public String toString() {
            return String.format("Direction.%s(%d, %s)", this.name(), getValue(), getSymbol());
        }
    }
}
