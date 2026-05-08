<template>
  <div class="permission-container">
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
        <el-table-column prop="permName" label="权限名称" width="200"></el-table-column>
        <el-table-column prop="permCode" label="权限编码" width="200"></el-table-column>
        <el-table-column prop="permType" label="类型" width="100">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.permType === 1" size="small">菜单</el-tag>
            <el-tag v-else-if="scope.row.permType === 2" size="small" type="warning">按钮</el-tag>
            <el-tag v-else size="small" type="danger">接口</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="path" label="路径" width="200"></el-table-column>
        <el-table-column prop="icon" label="图标" width="100"></el-table-column>
        <el-table-column prop="sort" label="排序" width="80"></el-table-column>
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
        <el-form-item label="权限名称" prop="permName">
          <el-input v-model="form.permName" placeholder="请输入权限名称"></el-input>
        </el-form-item>
        <el-form-item label="权限编码" prop="permCode">
          <el-input v-model="form.permCode" placeholder="请输入权限编码"></el-input>
        </el-form-item>
        <el-form-item label="类型" prop="permType">
          <el-select v-model="form.permType" placeholder="请选择类型" style="width: 100%;">
            <el-option label="菜单" :value="1"></el-option>
            <el-option label="按钮" :value="2"></el-option>
            <el-option label="接口" :value="3"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="上级权限" prop="parentId">
          <el-input-number v-model="form.parentId" :min="0" style="width: 100%;"></el-input-number>
        </el-form-item>
        <el-form-item label="路径" prop="path">
          <el-input v-model="form.path" placeholder="请输入路径"></el-input>
        </el-form-item>
        <el-form-item label="图标" prop="icon">
          <el-input v-model="form.icon" placeholder="请输入图标 class"></el-input>
        </el-form-item>
        <el-form-item label="排序" prop="sort">
          <el-input-number v-model="form.sort" :min="0" :max="999" style="width: 100%;"></el-input-number>
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
import { getPermissionTree, addPermission, updatePermission, deletePermission } from '@/api'

export default {
  name: 'Permission',
  data() {
    return {
      loading: false,
      tableData: [],
      expandedKeys: [],
      dialogVisible: false,
      dialogTitle: '',
      form: {
        id: null,
        permName: '',
        permCode: '',
        permType: 1,
        parentId: 0,
        path: '',
        icon: '',
        sort: 0
      },
      rules: {
        permName: [{ required: true, message: '请输入权限名称', trigger: 'blur' }],
        permCode: [{ required: true, message: '请输入权限编码', trigger: 'blur' }]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      getPermissionTree().then(res => {
        this.tableData = res.data || []
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    handleAdd() {
      this.dialogTitle = '新增权限'
      this.form = {
        id: null,
        permName: '',
        permCode: '',
        permType: 1,
        parentId: 0,
        path: '',
        icon: '',
        sort: 0
      }
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.dialogTitle = '编辑权限'
      this.form = { ...row }
      this.dialogVisible = true
    },
    handleDelete(row) {
      this.$confirm('确认删除该权限吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deletePermission(row.id).then(() => {
          this.$message.success('删除成功')
          this.getList()
        })
      })
    },
    submitForm() {
      this.$refs.form.validate(valid => {
        if (valid) {
          const api = this.form.id ? updatePermission : addPermission
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
.permission-container {
  padding: 20px;
}
</style>
