15-456 Computational Geometry Project

Members: Junjie Liang (junjieli)
         Hui Han Chin (hchin)

1. What does our program do
	
	Given a point set in R^2 our program would compute simplicial depth of each point and perform a lifting into R^3 in which the depth is visualized as the 3rd spatial coordinate

2. How do i get it run
	The program would be deployed as a java applet. It would be written in Processing.

3. What is the input
	The input can be done manually by clicking on the plane. Also we can randomly generate a point set given a integer.

4. How does the basic algorithm work?
	We can compute the simplicial depth of a point by implementing an algorithm described in the paper "Geometric Medians", by J. Gil, W. Steiger and A. Wigderson(Discrete Mathematics 108 (1992), 37-51).
	To extend the stated algorithm to find the simplicial depth of points in R^2, we will determine all intersections in our point set P, and work out the simplicial depth in each convex region (within each region the simplicial depth will be the same). Thereafter we just need to implement the lifting to visualize the output.

