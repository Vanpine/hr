<template>
  <div class="department-container">
    <el-card>
      <!-- 操作栏 -->
      <el-row :gutter="10" style="margin-bottom: 15px;">
        <el-col :span="1.5">
          <el-button type="primary" @click="handleAdd">新增</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button type="success" @click="handleExpand">展开/收起</el-button>
        </el-col>
      </el-row>

      <!-- 表格 -->
      <el-table
        v-loading="loading"
        :data="tableData"
        row-key="id"
        :expand-row-keys="expandedKeys"
        :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
        border
      >
        <el-table-column prop="deptName" label="部门名称" width="200"></el-table-column>
        <el-table-column prop="deptCode" label="部门编码" width="150"></el-table-column>
        <el-table-column prop="sort" label="排序" width="100"></el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'info'">
              {{ scope.row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
        <el-table-column label="操作" fixed="right" width="180">
          <template slot-scope="scope">
            <el-button type="text" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="text" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="500px">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="部门名称" prop="deptName">
          <el-input v-model="form.deptName" placeholder="请输入部门名称"></el-input>
        </el-form-item>
        <el-form-item label="部门编码" prop="deptCode">
          <el-input v-model="form.deptCode" placeholder="请输入部门编码"></el-input>
        </el-form-item>
        <el-form-item label="上级部门" prop="parentId">
          <el-select v-model="form.parentId" placeholder="请选择上级部门" style="width: 100%;">
            <el-option label="顶级部门" :value="0"></el-option>
            <el-option
              v-for="dept in deptList"
              :key="dept.id"
              :label="dept.deptName"
              :value="dept.id"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="排序" prop="sort">
          <el-input-number v-model="form.sort" :min="0" :max="999" style="width: 100%;"></el-input-number>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <span slot="footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">确定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { getDepartmentTree, addDepartment, updateDepartment, deleteDepartment, getAllDepartments } from '@/api'

export default {
  name: 'Department',
  data() {
    return {
      loading: false,
      tableData: [],
      deptList: [],
      expandedKeys: [],
      dialogVisible: false,
      dialogTitle: '',
      form: {
        id: null,
        deptName: '',
        deptCode: '',
        parentId: 0,
        sort: 0,
        status: 1
      },
      rules: {
        deptName: [{ required: true, message: '请输入部门名称', trigger: 'blur' }],
        deptCode: [{ required: true, message: '请输入部门编码', trigger: 'blur' }]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      getDepartmentTree().then(res => {
        this.tableData = res.data || []
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
      getAllDepartments().then(res => {
        this.deptList = res.data || []
      })
    },
    handleAdd() {
      this.dialogTitle = '新增部门'
      this.form = {
        id: null,
        deptName: '',
        deptCode: '',
        parentId: 0,
        sort: 0,
        status: 1
      }
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.dialogTitle = '编辑部门'
      this.form = { ...row }
      this.dialogVisible = true
    },
    handleDelete(row) {
      this.$confirm('确认删除该部门吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteDepartment(row.id).then(() => {
          this.$message.success('删除成功')
          this.getList()
        }).catch(err => {
          this.$message.error(err.message || '删除失败')
        })
      })
    },
    submitForm() {
      this.$refs.form.validate(valid => {
        if (valid) {
          const api = this.form.id ? updateDepartment : addDepartment
          api(this.form).then(() => {
            this.$message.success(this.form.id ? '修改成功' : '新增成功')
            this.dialogVisible = false
            this.getList()
          })
        }
      })
    },
    handleExpand() {
      if (this.expandedKeys.length > 0) {
        this.expandedKeys = []
      } else {
        this.expandedKeys = this.tableData.map(item => item.id)
      }
    }
  }
}
</script>

<style scoped>
.department-container {
  padding: 20px;
}
</style>
