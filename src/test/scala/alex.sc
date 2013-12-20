val m: Map[String, Long] = Map("test 1" -> System.nanoTime(), "test 2" -> System.nanoTime(), "test 3" ->System.nanoTime())


println(m)

val l = m.toList.sortBy(tuple => tuple._2).map(tuple => tuple._1)

println(l)

