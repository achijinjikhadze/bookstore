package javabookstore.authors;

public interface authorintf {
	void insertAuthor(Author author);
    void updateAuthor(Author author);
    void deleteAuthor(int id);
    void printAllAuthors();
}
