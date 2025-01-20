package F64.Board;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    @Transactional
    @Modifying
    @Query("UPDATE Board b SET b.viewCount = b.viewCount + 1 WHERE b.id = :id")
    void increaseViewCount(Long id);

    @Transactional
    @Modifying
    @Query("DELETE FROM Board b WHERE b.id = :boardId")
    void deleteByBoardId(@Param("boardId") Long boardId);

    List<Board> findAllByOrderByCreatedDateDesc();

    @Query("SELECT b FROM Board b ORDER BY b.createdDate DESC")
    Page<Board> findAllWithSorting(Pageable pageable);

}
