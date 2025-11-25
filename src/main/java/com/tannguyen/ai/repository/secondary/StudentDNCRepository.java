package com.tannguyen.ai.repository.secondary;

import com.tannguyen.ai.model.secondary.student.StudentDNC;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StudentDNCRepository extends JpaRepository<StudentDNC, String>, JpaSpecificationExecutor<StudentDNC>, StudentFacetRepository {

    @Query("select distinct s.coSo from StudentDNC s where s.coSo is not null and s.coSo <> ''")
    List<String> distinctCoSo();

    @Query("select distinct s.khoa from StudentDNC s where s.khoa is not null and s.khoa <> ''")
    List<String> distinctKhoa();

    @Query("select distinct s.nganh from StudentDNC s where s.nganh is not null and s.nganh <> ''")
    List<String> distinctNganh();

    @Query("select distinct s.gioiTinh from StudentDNC s where s.gioiTinh is not null and s.gioiTinh <> ''")
    List<String> distinctGioiTinh();

    @Query("select distinct s.bacDaoTao from StudentDNC s where s.bacDaoTao is not null and s.bacDaoTao <> ''")
    List<String> distinctBacDaoTao();

    @Query("select distinct s.loaiHinhDaoTao from StudentDNC s where s.loaiHinhDaoTao is not null and s.loaiHinhDaoTao <> ''")
    List<String> distinctLoaiHinhDaoTao();
}