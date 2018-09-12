#Power Iteration

In mathematics, the power iteration is an eigenvalue algorithm: given a matrix A, the algorithm will produce a number λ (the eigenvalue) and a nonzero vector v (the eigenvector), such that Av = λv. The algorithm is also known as the Von Mises iteration.[1]
The power iteration is a very simple algorithm. It does not compute a matrix decomposition, and hence it can be used when A is a very large sparse matrix. However, it will find only one eigenvalue (the one with the greatest absolute value) and it may converge only slowly.

#Methods

##Regular Iteration

Find the largest EigenValue.

##Inverse Iteration

Find the smallest EigenValue.

##Shifted Iteration

#Applications

Power iteration is not used very much because it can find only the dominant eigenvalue. Nevertheless, the algorithm is very useful in some specific situations. For instance, Google uses it to calculate the PageRank of documents in their search engine.[2] For matrices that are well-conditioned and as sparse as the web matrix, the power iteration method can be more efficient than other methods of finding the dominant eigenvector.

