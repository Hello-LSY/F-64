package F64.Board.Deleted;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface DeletedBoardRepository extends JpaRepository<DeletedBoard, Long> {
    @Transactional
    boolean existsByBoardId(Long boardId);
}
