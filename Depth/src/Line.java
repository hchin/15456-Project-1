
class Line {
	double m, c;

	public Line(double m, double c) {
		this.m = m;
		this.c = c;
	}

	/*
	 * Lines are not parallel, and do not have infinite gradient
	 * This is guaranteed since all lines are dual of points
	 */
	public Point intersect(Line line) {
		double x = (line.c - this.c) / (this.m - line.m);
		double y = this.m * x + this.c;

		return new Point(x, y);
	}

	public double evalAtX(double x) {
		return m * x + c;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(c);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(m);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Line other = (Line) obj;
		if (Double.doubleToLongBits(c) != Double.doubleToLongBits(other.c))
			return false;
		if (Double.doubleToLongBits(m) != Double.doubleToLongBits(other.m))
			return false;
		return true;
	}
}

