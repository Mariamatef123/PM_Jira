// var campaigns = [
// ];

var editingId = null;

// // فتح المودال
function openModal(id = null) {
  editingId = id;
  document.getElementById("modal-overlay").style.display = "flex";

  if (id) {
    var campaign = campaigns.find(c => c.id === id);

    document.getElementById("c-name").value = campaign.title;
    document.getElementById("c-location").value = campaign.location;
    document.getElementById("c-date").value = campaign.date;
    document.getElementById("c-description").value = campaign.description || "";
  } else {
    resetForm();
  }
}

// // قفل
function closeModal() {
  document.getElementById("modal-overlay").style.display = "none";
  resetForm();
}

// // Reset
function resetForm() {
  document.getElementById("c-name").value = "";
  document.getElementById("c-location").value = "";
  document.getElementById("c-date").value = "";

  document.getElementById("c-description").value = "";

}

// // Save
// function saveCampaign() {
//   var title = document.getElementById("c-name").value.trim();
//   var location = document.getElementById("c-location").value.trim();
//   var date = document.getElementById("c-date").value;
//   var description = document.getElementById("c-description").value.trim();

//   if (!title || !location || !date ) {
//     alert("Fill all required fields");
//     return;
//   }

//   var campaign = {
//     id: editingId || Date.now(),
//     title,
//     location,
//     date,

//     description
//   };

//   if (editingId) {
//     campaigns = campaigns.map(c => c.id === editingId ? campaign : c);
//   } else {
//     campaigns.push(campaign);
//   }

//   closeModal();
//   render();
// }

// // Delete
// function deleteCampaign(id) {
//   campaigns = campaigns.filter(c => c.id !== id);
//   render();
// }

// // Render
// function render() {
//   var tbody = document.getElementById("campaign-tbody");
//   var cards = document.getElementById("campaign-cards");

//   tbody.innerHTML = "";
//   cards.innerHTML = "";

//   campaigns.forEach(c => {
//     // TABLE
//     tbody.innerHTML += `
//       <tr>
//         <td>${c.name}</td>
//         <td>${c.location}</td>
//         <td>${c.date}</td>

 
//         <td>
//           <button onclick="openModal(${c.id})">Edit</button>
//           <button onclick="deleteCampaign(${c.id})">Delete</button>
//         </td>
//       </tr>
//     `;

//     // CARDS
//     cards.innerHTML += `
//       <div class="campaign-card">
//         <h3>${c.title}</h3>
//         <p>${c.description || "No description"}</p>
//         <small>📍 ${c.location} | 📅 ${c.date}</small>
//       </div>
//     `;
//   });
// }

// // Events
// document.getElementById("add-campaign-btn").onclick = () => openModal();
// document.getElementById("save-btn").onclick = saveCampaign;
// document.getElementById("cancel-btn").onclick = closeModal;
// document.getElementById("modal-close-btn").onclick = closeModal;

// render();