package endpoint

object select {
  class from[T](data: List[T]) {
    def where(function: T => Boolean): List[T] = data.filter(t => function(t))
  }

  def from[T](data: List[T]): from[T] = new from(data)
}
