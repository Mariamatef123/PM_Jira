function renderVolunteers() {
  const tbody = document.getElementById('vol-tbody');

  tbody.innerHTML = volunteers.map(v => `
    <tr>
      <td>${v.name}</td>
      <td>${v.email}</td>

      <td>
        <span style="
          padding:4px 10px;
          border-radius:20px;
          font-size:12px;
          background:${v.status==='approved' ? '#e8f8ef' : v.status==='rejected' ? '#fdecea' : '#fff8e1'};
          color:${v.status==='approved' ? '#1e7e4a' : v.status==='rejected' ? '#c0392b' : '#c67c00'};
        ">
          ${v.status}
        </span>
      </td>

      <td>${v.assignedTo || '—'}</td>

      <td>
        <button onclick="approve(${v.id})">Approve</button>
        <button onclick="reject(${v.id})">Reject</button>
        <button onclick="assign(${v.id})">Assign</button>
      </td>
    </tr>
  `).join('');
}