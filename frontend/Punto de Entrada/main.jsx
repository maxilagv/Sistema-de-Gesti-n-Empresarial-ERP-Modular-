import React from 'react';
import { createRoot } from 'react-dom/client';

// Aplica preferencia de tema si existe ("light" | "dark" | null -> auto)
(() => {
  try {
    const pref = localStorage.getItem('theme');
    const root = document.documentElement;
    if (pref === 'dark') root.setAttribute('data-theme', 'dark');
    else if (pref === 'light') root.setAttribute('data-theme', 'light');
    else root.removeAttribute('data-theme');
  } catch (_) {
    // Ignorar en modo privado / bloqueos de storage
  }
})();

function AppShell() {
  return (
    <div className="app-shell">
      <main className="container">
        <div className="card" role="region" aria-label="Bienvenida">
          <h1>ERP</h1>
          <p className="muted">Base visual lista. Agrega rutas y vistas cuando quieras.</p>
          <div className="row gap-md">
            <a className="btn btn--primary" href="#">Acción primaria</a>
            <a className="btn btn--ghost" href="#">Secundaria</a>
          </div>
        </div>
      </main>
    </div>
  );
}

const root = createRoot(document.getElementById('root'));
root.render(<AppShell />);
