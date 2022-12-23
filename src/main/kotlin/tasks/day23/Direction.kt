package tasks.day23

//             y:
//             -3
//             -2
//             -1
//x:  -3 -2 -1  0  1  2  3
//              1
//              2
//              3
//              4

enum class Direction(val x: Int, val y: Int) {
    N(0, -1),
    NE(1, -1),
    E(1, 0),
    SE(1, 1),
    S(0, 1),
    SW(-1, 1),
    W(-1, 0),
    NW(-1, -1)
}






//enum class Direction(val x: Int, val y: Int) {
//    N(0, 1),
//    NE(1, 1),
//    E(1, 0),
//    SE(1, -1),
//    S(0, -1),
//    SW(-1, -1),
//    W(-1, 0),
//    NW(-1, 1)
//}