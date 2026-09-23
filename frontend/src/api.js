import axios from 'axios';

const client = axios.create({ baseURL: import.meta.env.VITE_API_BASE_URL || '/api' });

export const dashboardService = {
  summary: () => client.get('/dashboard/summary'),
  metrics: () => client.get('/dashboard/metrics'),
  orders: () => client.get('/dashboard/orders'),
  inventory: () => client.get('/dashboard/inventory'),
};

export const productService = {
  list: () => client.get('/products'),
  create: (payload) => client.post('/products', payload),
  update: (id, payload) => client.put(`/products/${id}`, payload),
  remove: (id) => client.delete(`/products/${id}`),
  adjust: (id, payload) => client.post(`/products/${id}/adjust`, payload),
  movements: (id) => client.get(`/products/${id}/movements`),
};

export const orderService = {
  list: (params) => client.get('/orders', { params }),
  create: (payload) => client.post('/orders', payload),
  status: (id, value) => client.put(`/orders/${id}/status`, null, { params: { value } }),
  cancel: (id) => client.post(`/orders/${id}/cancel`),
  simulate: (count, payload) => client.post(`/simulation/orders?numberOfOrders=${count}`, payload),
};

export const customerService = {
  list: (search) => client.get('/customers', { params: search ? { search } : {} }),
  create: (payload) => client.post('/customers', payload),
  update: (id, payload) => client.put(`/customers/${id}`, payload),
  remove: (id) => client.delete(`/customers/${id}`),
};

export const supplierService = {
  list: (search) => client.get('/suppliers', { params: search ? { search } : {} }),
  create: (payload) => client.post('/suppliers', payload),
  update: (id, payload) => client.put(`/suppliers/${id}`, payload),
  remove: (id) => client.delete(`/suppliers/${id}`),
};

export const inventoryService = {
  movements: () => client.get('/inventory/movements'),
};

export default client;
