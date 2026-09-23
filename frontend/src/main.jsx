import React, { useEffect, useMemo, useState } from 'react';
import { createRoot } from 'react-dom/client';
import { Bar, BarChart, CartesianGrid, ResponsiveContainer, Tooltip, XAxis, YAxis } from 'recharts';
import axios from 'axios';
import './styles.css';
import App from './App';

const api = axios.create({ baseURL: '/api' });

const empty = {
  summary: {
    total: 0,
    pending: 0,
    processing: 0,
    completed: 0,
    out_of_stock: 0,
    dead_letter: 0,
  },
  orders: [],
  inventory: [],
};

const metricMeta = [
  { key: 'total', label: 'Total orders', accent: 'blue' },
  { key: 'processing', label: 'Processing', accent: 'amber' },
  { key: 'completed', label: 'Completed', accent: 'green' },
  { key: 'out_of_stock', label: 'Out of stock', accent: 'red' },
  { key: 'dead_letter', label: 'Dead letter', accent: 'violet' },
];

const formatStatus = (value) => String(value ?? 'pending').toLowerCase().replace(/_/g, ' ');
const statusClassMap = {
  pending: 'pending',
  processing: 'processing',
  completed: 'completed',
  'out of stock': 'out_of_stock',
  'dead letter': 'dead_letter',
};

function LegacyDashboard() {
  const [data, setData] = useState(empty);
  const [connected, setConnected] = useState(false);
  const [form, setForm] = useState({ customerName: 'Demo operator', productId: '1', quantity: 1 });
  const [testCount, setTestCount] = useState(20);
  const [message, setMessage] = useState('');

  const reload = () =>
    Promise.all([
      api.get('/dashboard/summary'),
      api.get('/dashboard/orders'),
      api.get('/dashboard/inventory'),
    ])
      .then(([summary, orders, inventory]) => {
        setData({
          summary: summary.data ?? empty.summary,
          orders: orders.data ?? [],
          inventory: inventory.data ?? [],
        });
      })
      .catch(() => setMessage('Backend unavailable. Start Spring Boot on port 8080.'));

  useEffect(() => {
    reload();
    const stream = new EventSource('/api/orders/events');
    stream.onopen = () => setConnected(true);
    stream.onerror = () => setConnected(false);
    stream.onmessage = () => reload();
    return () => stream.close();
  }, []);

  const createOrder = async (event) => {
    event.preventDefault();
    try {
      await api.post('/orders', {
        ...form,
        productId: Number(form.productId),
        quantity: Number(form.quantity),
      });
      setMessage('Order accepted by the worker pool.');
      reload();
    } catch (error) {
      setMessage(error.response?.data?.message || 'Could not create order.');
    }
  };

  const runTest = async () => {
    try {
      const response = await api.post(`/simulation/orders?numberOfOrders=${testCount}`, {
        customerName: 'Concurrency test',
        productId: Number(form.productId),
        quantity: 1,
      });
      setMessage(`Submitted ${response.data.requested} orders in ${response.data.durationMs} ms.`);
      reload();
    } catch {
      setMessage('Concurrency test failed.');
    }
  };

  const chartData = ['pending', 'processing', 'completed', 'out_of_stock', 'dead_letter'].map((key) => ({
    name: key.replace('_', ' '),
    value: Number(data.summary[key] || 0),
  }));

  const inventoryCards = useMemo(() => (data.inventory || []).slice(0, 4), [data.inventory]);
  const recentOrders = useMemo(() => (data.orders || []).slice(0, 6), [data.orders]);

  const totalAvailable = (data.inventory || []).reduce((sum, item) => sum + (item.availableInventory ?? 0), 0);
  const totalStock = (data.inventory || []).reduce((sum, item) => sum + (item.totalInventory ?? 0), 0);

  return (
    <div className="app-shell">
      <aside className="sidebar">
        <div className="brand-block">
          <div className="brand-mark">OP</div>
          <div>
            <h2>OrderPulse</h2>
            <span>Live operations</span>
          </div>
        </div>

        <nav className="side-nav">
          <button className="nav-item active">Overview <span>01</span></button>
          <button className="nav-item">Orders <span>{data.summary.total || 0}</span></button>
          <button className="nav-item">Inventory</button>
          <button className="nav-item">Failures</button>
          <button className="nav-item">Dead-letter</button>
          <button className="nav-item">Concurrency lab</button>
        </nav>

        <div className="engine-card">
          <span className="eyebrow">Processing engine</span>
          <strong>5 core workers</strong>
          <small>Queue health 94%</small>
        </div>
      </aside>

      <main className="main-panel">
        <header className="topbar">
          <div>
            <p className="eyebrow">Operations / control room</p>
            <h1>Real-time order orchestration</h1>
          </div>

          <div className={`connect-pill ${connected ? 'online' : 'offline'}`}>
            <span className="dot" />
            {connected ? 'Live stream connected' : 'Waiting for stream'}
          </div>
        </header>

        <section className="hero-panel">
          <div className="hero-copy">
            <span className="eyebrow accent">High-throughput commerce</span>
            <h2>Inventory remains protected even under concurrent demand.</h2>
            <p>
              OrderPulse demonstrates a production-style flow where reservation, locking, async processing,
              retries, and live observability work together in one operating system.
            </p>

            <div className="cta-row">
              <button className="primary-btn" onClick={() => window.scrollTo({ top: document.body.scrollHeight, behavior: 'smooth' })}>Launch order</button>
              <button className="ghost-btn" onClick={runTest}>Run concurrency lab</button>
            </div>

            <div className="mini-stats">
              <div>
                <strong>{data.summary.total || 0}</strong>
                <span>orders</span>
              </div>
              <div>
                <strong>{totalAvailable}</strong>
                <span>stock left</span>
              </div>
              <div>
                <strong>{totalStock}</strong>
                <span>total units</span>
              </div>
            </div>
          </div>

          <div className="hero-visual">
            <div className="glass-card pulse-card">
              <div className="pulse-header">
                <span className="eyebrow">System signal</span>
                <span className="live-tag">Live</span>
              </div>

              <div className="signal-ring">
                <div className="ring-core">
                  <strong>{data.summary.completed || 0}</strong>
                  <span>completed</span>
                </div>
              </div>

              <ul>
                <li><span className="tiny-dot blue" /> 5 workers online</li>
                <li><span className="tiny-dot green" /> Locking active</li>
                <li><span className="tiny-dot amber" /> Queue stable</li>
              </ul>
            </div>
          </div>
        </section>

        <section className="metrics-grid">
          {metricMeta.map(({ key, label, accent }) => (
            <div className="metric-card" key={key}>
              <div className={`metric-dot ${accent}`} />
              <small>{label}</small>
              <strong>{data.summary[key] || 0}</strong>
              <span>live</span>
            </div>
          ))}
        </section>

        <section className="content-grid">
          <div className="panel chart-panel">
            <div className="panel-header">
              <div>
                <span className="eyebrow">Throughput</span>
                <h3>Order states</h3>
              </div>
              <span className="live-tag">Live</span>
            </div>

            <div className="chart-wrap">
              <ResponsiveContainer width="100%" height={240}>
                <BarChart data={chartData}>
                  <CartesianGrid strokeDasharray="3 3" vertical={false} stroke="rgba(148,163,184,0.18)" />
                  <XAxis dataKey="name" tick={{ fill: '#9aaac0', fontSize: 11 }} axisLine={false} tickLine={false} />
                  <YAxis allowDecimals={false} tick={{ fill: '#9aaac0', fontSize: 11 }} axisLine={false} tickLine={false} />
                  <Tooltip
                    cursor={{ fill: 'rgba(148,163,184,0.08)' }}
                    contentStyle={{ background: '#0f1f2f', border: '1px solid rgba(255,255,255,0.08)', borderRadius: 12, color: '#e5edf9' }}
                  />
                  <Bar dataKey="value" fill="#5aa9ff" radius={[8, 8, 0, 0]} barSize={30} />
                </BarChart>
              </ResponsiveContainer>
            </div>
          </div>

          <div className="panel form-panel">
            <div className="panel-header">
              <div>
                <span className="eyebrow">Intake</span>
                <h3>Submit an order</h3>
              </div>
            </div>

            <form onSubmit={createOrder} className="order-form">
              <label>
                Customer
                <input value={form.customerName} onChange={(event) => setForm({ ...form, customerName: event.target.value })} />
              </label>

              <div className="split-fields">
                <label>
                  Product ID
                  <input type="number" value={form.productId} onChange={(event) => setForm({ ...form, productId: event.target.value })} />
                </label>

                <label>
                  Quantity
                  <input type="number" min="1" value={form.quantity} onChange={(event) => setForm({ ...form, quantity: event.target.value })} />
                </label>
              </div>

              <div className="test-row">
                <label>
                  Load test size
                  <input type="number" min="1" value={testCount} onChange={(event) => setTestCount(Number(event.target.value))} />
                </label>
              </div>

              <div className="form-actions">
                <button type="submit" className="primary-btn">Send order</button>
                <button type="button" className="ghost-btn" onClick={runTest}>Run batch</button>
              </div>

              {message && <p className="status-message">{message}</p>}
            </form>
          </div>
        </section>

        <section className="bottom-grid">
          <div className="panel inventory-panel">
            <div className="panel-header">
              <div>
                <span className="eyebrow">Inventory</span>
                <h3>Live stock signal</h3>
              </div>
            </div>

            <div className="inventory-list">
              {inventoryCards.map((item) => {
                const percent = item.totalInventory ? ((item.availableInventory ?? 0) / item.totalInventory) * 100 : 0;
                return (
                  <div className="inventory-row" key={item.id}>
                    <div className="inventory-topline">
                      <strong>{item.name}</strong>
                      <span>{item.availableInventory ?? 0} / {item.totalInventory}</span>
                    </div>
                    <div className="progress-track">
                      <span style={{ width: `${Math.max(percent, 8)}%` }} />
                    </div>
                  </div>
                );
              })}
            </div>
          </div>

          <div className="panel orders-panel">
            <div className="panel-header">
              <div>
                <span className="eyebrow">Activity</span>
                <h3>Recent orders</h3>
              </div>
            </div>

            <div className="orders-list">
              {recentOrders.map((order) => {
                const status = formatStatus(order.status);
                const statusClass = statusClassMap[status] || 'pending';
                return (
                  <div className="order-row" key={order.id || order.orderNumber}>
                    <div>
                      <strong>{order.orderNumber || `ORD-${order.id}`}</strong>
                      <span>{order.customerName}</span>
                    </div>
                    <div>
                      <strong>{order.product || 'Product'}</strong>
                      <span>{order.quantity} units</span>
                    </div>
                    <span className={`status-pill status-${statusClass}`}>{status}</span>
                  </div>
                );
              })}
            </div>
          </div>
        </section>
      </main>
    </div>
  );
}

createRoot(document.getElementById('root')).render(
  <React.StrictMode>
    <App />
  </React.StrictMode>
);
