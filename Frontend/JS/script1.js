


// var pending = [
//   { id: 1, name: "Sara Ahmed", email: "sara@gmail.com", phone: "+20 100 555 2101" },
//   { id: 2, name: "Omar Ali", email: "omar@gmail.com", phone: "+20 111 222 3344" },
//   { id: 3, name: "Nour Hossam", email: "nour@gmail.com", phone: "+20 122 987 4561" }
// ];




// var staff = [
//   { id: 21, name: "Ahmed Mostafa", email: "ahmed.hr@br.com", phone: "+20 100 000 0000", dept: "HR" },
//   { id: 22, name: "Mona Samir", email: "mona.hr@br.com", phone: "+20 111 111 1111", dept: "IT" }
// ];

// var pageContent = {
//   pending: {
//     title: "Pending Volunteers",
//     subtitle: "Review volunteer requests and decide who joins the team."
//   },

//   staff: {
//     title: "Staff Members",
//     subtitle: "View all staff members and update or delete their data."
//   }
// };


// function showPage(page) {
//   var pages = ["pending", "staff"];

//   pages.forEach(function (pageName) {
//     var sectionId = pageName === "pending" ? "pendingPage" : pageName === "volunteers" ? "volunteerPage" : "staffPage";
//     var section = document.getElementById(sectionId);
//     var navLink = document.querySelector('[data-page="' + pageName + '"]');

//     if (pageName === page) {
//       section.classList.add("active");
//       navLink.classList.add("active");
//     } else {
//       section.classList.remove("active");
//       navLink.classList.remove("active");
//     }
//   });

 
//   document.getElementById("page-title").textContent = pageContent[page].title;
//   document.getElementById("page-subtitle").textContent = pageContent[page].subtitle;
// }


// function updateSummary() {
//   document.getElementById("pending-count").textContent = pending.length;

//   document.getElementById("staff-count").textContent = staff.length;
// }

// function renderPending() {
//   var table = document.getElementById("pendingTable");
//   var emptyState = document.getElementById("pending-empty");
//   var rows = "";


//   if (pending.length === 0) {
//     table.innerHTML = "";
//     emptyState.style.display = "grid";
//     return;
//   }



//   emptyState.style.display = "none";
// pending.forEach(function (volunteer) {
//     rows += "<tr>";
//     rows += "<td><strong>" + volunteer.name + "</strong></td>";
//     rows += "<td>" + volunteer.email + "</td>";
//     rows += "<td>" + volunteer.phone + "</td>";
//     rows += '<td><select class="dept-select" id="dept-' + volunteer.id + '">';
//     rows += '<option value="HR">HR</option>';
//     rows += '<option value="IT">IT</option>';
//     rows += '<option value="Operations">Operations</option>';
//     rows += '<option value="Media">Media</option>';
//     rows += "</select></td>";
//     rows += '<td><div class="actions">';
//     rows += '<button class="btn-action accept" type="button" data-accept-id="' + volunteer.id + '">Accept</button>';
//     rows += '<button class="btn-action reject" type="button" data-reject-id="' + volunteer.id + '">Reject</button>';
//     rows += "</div></td>";
//     rows += "</tr>";
//   });

//   table.innerHTML = rows;
// }



// function renderStaff() {
//   var table = document.getElementById("staffTable");
//   var emptyState = document.getElementById("staff-empty");
//   var rows = "";

//   if (staff.length === 0) {
//     table.innerHTML = "";
//     emptyState.style.display = "grid";
//     return;
//   }

//   emptyState.style.display = "none";

//   staff.forEach(function (member) {
//     rows += "<tr>";
//     rows += "<td><strong>" + member.name + "</strong></td>";
//     rows += "<td>" + member.email + "</td>";
//     rows += "<td>" + member.phone + "</td>";
//     rows += "<td>" + member.dept + "</td>";
//     rows += '<td><div class="actions">';
//     rows += '<button class="btn-action edit" data-edit-staff-id="' + member.id + '">Update</button>';
//     rows += '<button class="btn-action delete" data-delete-staff-id="' + member.id + '">Delete</button>';
//     rows += "</div></td>";
//     rows += "</tr>";
//   });

//   table.innerHTML = rows;
// }

// function render() {
//   updateSummary();
//   renderPending();
//   renderStaff(); 
// }

// function accept(id) {
//   var volunteer = pending.find(function (item) {
//     return item.id === id;
//   });

//   var deptField = document.getElementById("dept-" + id);
//   var dept = deptField ? deptField.value : "HR";

//   if (!volunteer) return;

//   // ➕ إضافة للـ staff
//   staff.unshift({
//     id: Date.now(),
//     name: volunteer.name,
//     email: volunteer.email,
//     phone: volunteer.phone,
//     dept: dept
//   });

//   // ❌ حذف من pending
//   pending = pending.filter(function (item) {
//     return item.id !== id;
//   });

//   render();
// }

// function reject(id) {
//   pending = pending.filter(function (item) {
//     return item.id !== id;
//   });
//   render();
// }





// function deleteStaff(id) {
//   staff = staff.filter(function (member) {
//     return member.id !== id;
//   });
//   render();
// }




  
// function editStaff(id) {
//   var member = staff.find(function (item) {
//     return item.id === id;
//   });
//   var newName;
//   var newRole;

//   if (!member) {
//     return;
//   }

//   newName = prompt("Update staff name:", member.name);
//   if (!newName) {
//     return;
//   }

 
//   newRole = prompt("Update staff role:", member.role);
//   if (!newRole) {
//     return;
//   }
// member.name = newName.trim();
//   member.role = newRole.trim();
//   render();
// }

// function bindEvents() {

//   var navLinks = document.querySelectorAll(".nav-link");
//   var pendingTable = document.getElementById("pendingTable");


//   var staffTable = document.getElementById("staffTable");

//   navLinks.forEach(function (button) {
//     button.addEventListener("click", function () {
//       showPage(button.getAttribute("data-page"));
//     });
//   });

//   pendingTable.addEventListener("click", function (event) {
//     var acceptId = event.target.getAttribute("data-accept-id");
//     var rejectId = event.target.getAttribute("data-reject-id");


//     if (acceptId) {
//       accept(Number(acceptId));
//     }

//     if (rejectId) {
//       reject(Number(rejectId));
//     }
//   });



//   staffTable.addEventListener("click", function (event) {
//     var editId = event.target.getAttribute("data-edit-staff-id");
//     var deleteId = event.target.getAttribute("data-delete-staff-id");

//     if (editId) {
   
//       editStaff(Number(editId));
//     }

//     if (deleteId) {
     
//       deleteStaff(Number(deleteId));
//     }
//   });
// }

// bindEvents();
// showPage("pending");
// render();