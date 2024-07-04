package jungsuk.ch12;

public class EnumEx1 {

    private static final Direction[] directions = Direction.values();

    public void ex1() {
        Direction direction1 = Direction.EAST;
        Direction direction2 = Direction.valueOf("WEST");
        Direction direction3 = Enum.valueOf(Direction.class, "EAST");

        System.out.println("direction1 = " + direction1);
        System.out.println("direction2 = " + direction2);
        System.out.println("direction3 = " + direction3);

        System.out.println("(direction1 == direction2) = " + (direction1 == direction2));
        System.out.println("(direction1 == direction3) = " + (direction1 == direction3));
        System.out.println("(direction1.equals(direction3)) = " + (direction1.equals(direction3)));
        // System.out.println("(direction2 > direction3) = " + (direction2 > direction3)); // compile error
        System.out.println("(direction1.compareTo(direction2)) = " + (direction1.compareTo(direction2))); // ordinal끼리의 계산이 됨 (0 - 2 = -2)
        System.out.println("(direction1.compareTo(direction3)) = " + (direction1.compareTo(direction3)));

        int randomIntFrom0To3 = (int) (Math.random() * 4);
        Direction randomDirection = directions[randomIntFrom0To3];

        switch (randomDirection) {
            // case Direction.EAST: // compile error - 열거형 switch case 라벨은 열거형 상수의 정규화되지 않은 이름이어야 합니다
            case EAST:
                System.out.println("current random direction is EAST");
                break;
            case SOUTH:
                System.out.println("current random direction is SOUTH");
                break;
            case WEST:
                System.out.println("current random direction is WEST");
                break;
            case NORTH:
                System.out.println("current random direction is NORTH");
                break;
        }

        for (Direction direction : directions) {
            System.out.printf("ordinal of %s = %d%n", direction.name(), direction.ordinal());
        }
    }

    enum Direction {
        EAST, SOUTH, WEST, NORTH
    }
}
